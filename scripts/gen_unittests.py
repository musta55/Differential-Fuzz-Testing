#!/usr/bin/env python3
"""
Generate a unit-test suite for every method in a project's manifest, with EvoSuite.

    gen_unittests.py <project> [--budget 60] [--jobs 8] [--max N] [--force] [--side original]

EvoSuite is run once per *class* (not per method — it generates whole-class suites) over the
compiled <Class>Original snapshots in target/test-classes, i.e. exactly the classes the fuzzer
targets. The suites are the input to two later steps:

    scripts/unit_coverage.py   how much of each method the unit suite covers
    scripts/extract_seeds.py   the concrete argument values, which become the fuzzer's seed corpus

Why the ORIGINAL side: the differential oracle asks whether the refactored method still behaves
like the original, so the original is the reference the seeds should exercise. Pass
--side refactored to generate against the other snapshot instead.

Outputs, all under target/evosuite/<project>/:
    evosuite-tests/<pkg>/<Class>Original_ESTest.java   the suite (plain JUnit 4, no scaffolding)
    evosuite-report/<class>/statistics.csv             EvoSuite's own coverage numbers, per class
    logs/<class>.log                                   that class's generation log
    generation.json                                    per-class status for later steps

Requires scripts/setup_evosuite.sh to have run, and a Java 8 JVM (set JAVA8_HOME if the default
java is newer — EvoSuite 1.2.0 uses JDK internals sealed off from Java 9 on).
"""
import argparse
import csv
import glob
import json
import os
import subprocess
import sys
import time
from concurrent.futures import ThreadPoolExecutor

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

# EvoSuite's own default criterion set, minus the two that cost the most for what they add here.
# WEAKMUTATION drives assertion quality, which we do not use (seeds are argument values, and the
# coverage question is about reaching code, not about killing mutants); dropping it roughly halves
# generation time on the apex classes. CBRANCH/METHODNOEXCEPTION stay: they push the search toward
# distinct call outcomes, which is what makes a seed interesting to the fuzzer.
CRITERIA = "LINE:BRANCH:EXCEPTION:METHOD:METHODNOEXCEPTION:CBRANCH:OUTPUT"


def evosuite_jar():
    """Locate evosuite-1.2.0.jar: the provisioned copy, $EVOSUITE_HOME, or a sibling checkout."""
    candidates = [
        os.path.join(MODULE, "tools/evosuite"),
        os.environ.get("EVOSUITE_HOME", ""),
        os.path.join(os.path.dirname(MODULE), "RefAgent-reproduce/evosuite"),
    ]
    for d in candidates:
        if not d:
            continue
        hits = sorted(glob.glob(os.path.join(d, "evosuite-[0-9]*.jar")))
        hits = [h for h in hits if "standalone" not in os.path.basename(h)]
        if hits:
            return hits[-1]
    sys.exit("EvoSuite jar not found. Run: scripts/setup_evosuite.sh")


def java8():
    """EvoSuite 1.2.0 needs a Java 8 JVM; the rest of the pipeline does not care."""
    home = os.environ.get("JAVA8_HOME")
    if home:
        return os.path.join(home, "bin/java")
    for d in ("/usr/lib/jvm/java-8-openjdk-amd64", "/usr/lib/jvm/java-1.8.0-openjdk-amd64"):
        if os.path.isfile(os.path.join(d, "bin/java")):
            return os.path.join(d, "bin/java")
    return "java"  # fall back to whatever is on PATH and let EvoSuite complain


def project_classpath(project):
    """target/test-classes plus the project profile's dependencies, as one -projectCP string."""
    cache = os.path.join(MODULE, "target", f"cp-{project}.txt")
    if not os.path.isfile(cache):
        r = subprocess.run([os.path.join(MODULE, "mvnw"), f"-P{project}", "-q",
                            "dependency:build-classpath", f"-Dmdep.outputFile={cache}",
                            "-DincludeScope=test"], cwd=MODULE, capture_output=True, text=True)
        if r.returncode != 0 or not os.path.isfile(cache):
            sys.exit(f"could not resolve the {project} classpath:\n{r.stdout}\n{r.stderr}")
    deps = open(cache).read().strip()
    classes = os.path.join(MODULE, "target/test-classes")
    return f"{classes}{os.pathsep}{deps}" if deps else classes


def target_classes(project, side):
    """The distinct snapshot classes to generate for, in manifest order (deduplicated)."""
    man = json.load(open(os.path.join(MODULE, "src/test/resources", project, "manifest.json")))
    out = []
    for e in man["methods"]:
        fqn = e[side]
        if fqn not in out:
            out.append(fqn)
    return out


def statistics(base):
    """EvoSuite's self-reported coverage per class, merged from the per-class report dirs.

    Each class gets its own report_dir: with --jobs > 1 several EvoSuite clients finish at once, and
    they all append to statistics.csv without locking, which interleaves and truncates rows.
    """
    rows = {}
    for path in glob.glob(os.path.join(base, "evosuite-report", "**", "statistics.csv"),
                          recursive=True):
        try:
            with open(path) as f:
                for row in csv.DictReader(f):
                    if row.get("TARGET_CLASS"):
                        rows[row["TARGET_CLASS"]] = row
        except (OSError, csv.Error):
            continue
    return rows


def ensure_compiled(project):
    """Compile this project's snapshots into target/test-classes before using them.

    Not optional and not merely a convenience: maven-compiler-plugin wipes the whole output
    directory when it notices the source roots changed, so building any OTHER profile deletes this
    project's classes. EvoSuite then reports "Unknown class" for every target and produces an empty
    run that looks like a tool failure (observed: 27/27 classes, 0 tests, 0 seconds each).
    """
    r = subprocess.run([os.path.join(MODULE, "mvnw"), f"-P{project}", "-q", "test-compile"],
                       cwd=MODULE, capture_output=True, text=True)
    if r.returncode != 0:
        sys.exit(f"test-compile failed for {project}:\n{r.stdout[-3000:]}\n{r.stderr[-2000:]}")


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("project")
    ap.add_argument("--budget", type=int, default=60, help="search seconds per class (default 60)")
    ap.add_argument("--max", type=int, default=0, help="only the first N classes (smoke test)")
    ap.add_argument("--side", default="original", choices=("original", "refactored"),
                    help="which snapshot to generate tests for (default original)")
    ap.add_argument("--force", action="store_true", help="regenerate suites that already exist")
    ap.add_argument("--jobs", type=int, default=8,
                    help="classes to generate in parallel (default 8; EvoSuite is one JVM each)")
    ap.add_argument("--timeout-slack", type=int, default=180,
                    help="seconds past --budget before a class is killed (default 180)")
    args = ap.parse_args()

    ensure_compiled(args.project)
    jar = evosuite_jar()
    java = java8()
    cp = project_classpath(args.project)
    base = os.path.join(MODULE, "target/evosuite", args.project)
    os.makedirs(base, exist_ok=True)

    classes = target_classes(args.project, args.side)
    if args.max:
        classes = classes[:args.max]
    print(f"EvoSuite {os.path.basename(jar)} on {len(classes)} {args.side} classes "
          f"({args.budget}s each, {args.jobs} at a time) -> {base}")

    done = [0]

    def generate(item):
        i, fqn = item
        rel = fqn.replace(".", "/")
        suite = os.path.join(base, "evosuite-tests", rel + "_ESTest.java")
        if os.path.isfile(suite) and not args.force:
            print(f"  [{i}/{len(classes)}] {fqn} — reusing existing suite", flush=True)
            return {"class": fqn, "status": "reused", "suite": suite,
                    "tests": open(suite).read().count("@Test")}
        cmd = [
            java, "-jar", jar,
            "-class", fqn,
            "-projectCP", cp,
            "-base_dir", base,
            # Per-class report dir: parallel clients append to statistics.csv without locking.
            "-Dreport_dir=" + os.path.join(base, "evosuite-report", rel.replace("/", ".")),
            "-criterion", CRITERIA,
            f"-Dsearch_budget={args.budget}",
            "-Dstopping_condition=MAXTIME",
            # Keep the client in-process budget aligned with the search budget, or EvoSuite's
            # 120s global_timeout silently caps a longer --budget.
            f"-Dglobal_timeout={args.budget}",
            "-Dassertion_timeout=20",
            "-Dminimization_timeout=20",
            # Compiling and re-running the suite to check stability costs as much as the search
            # itself on these classes, and a flaky test is still a perfectly good seed source.
            "-Djunit_check=FALSE",
            # Deterministic runs, so a rerun reproduces the same seeds.
            "-Dstrategy=MOSUITE",
            "-Dshow_progress=false",
            "-Dwrite_covered_goals_file=false",
            # Plain JUnit 4 output: no @RunWith(EvoRunner), no scaffolding, no separate
            # classloader. Not a cosmetic choice — EvoRunner loads its own instrumented copy of the
            # class under test, so JaCoCo records coverage against bytes that do not match the
            # class files on disk and scripts/unit_coverage.py reports 0/0 for every method
            # (measured: 9 tests ran, nothing covered). Standalone suites also make the seeds
            # readable as ordinary tests.
            "-Dno_runtime_dependency=true",
            "-Dreplace_calls=false",
            "-Dvirtual_fs=false",
            "-Duse_separate_classloader=false",
            # Required to make no_runtime_dependency actually produce compilable tests. A test that
            # tripped the sandbox is emitted wrapped in `executor.submit(...)`, and `executor` is a
            # scaffolding field that no_runtime_dependency stops generating — so the suite does not
            # compile (measured on apex-core: only 4 of 19 suites built, the rest failing on
            # "cannot find symbol: executor"). Dropping those tests keeps the sandbox itself ON,
            # which is what stops generation from writing real files while exercising DiskStorage.
            "-Dfilter_sandbox_tests=true",
        ]
        t0 = time.time()
        log = os.path.join(base, "logs", rel.replace("/", ".") + ".log")
        os.makedirs(os.path.dirname(log), exist_ok=True)
        # A class that outruns its budget must not take the run down with it. EvoSuite's own
        # timeouts do not always fire (a target that spawns threads can keep the client alive well
        # past global_timeout — FastDataList ran 4x its budget), and an uncaught TimeoutExpired here
        # would discard every suite generated so far.
        status = None
        work = os.path.join(base, "work")
        os.makedirs(work, exist_ok=True)
        try:
            with open(log, "w") as lf:
                r = subprocess.run(cmd, cwd=work, stdout=lf, stderr=subprocess.STDOUT,
                                   timeout=args.budget + args.timeout_slack)
            rc = r.returncode
        except subprocess.TimeoutExpired:
            rc, status = 1, "timeout"
        took = time.time() - t0
        ok = os.path.isfile(suite)
        if ok:
            status = "generated"
        elif status is None:
            status = "failed" if rc else "no-suite"
        ntests = open(suite).read().count("@Test") if ok else 0
        done[0] += 1
        print(f"  [{done[0]}/{len(classes)}] {fqn} — {status} ({ntests} tests, {took:.0f}s)",
              flush=True)
        return {"class": fqn, "status": status, "suite": suite if ok else None,
                "tests": ntests, "seconds": round(took, 1), "log": log}

    with ThreadPoolExecutor(max_workers=args.jobs) as pool:
        results = list(pool.map(generate, enumerate(classes, 1)))

    stats = statistics(base)
    for r in results:
        row = stats.get(r["class"])
        if row:
            r["evosuite_coverage"] = {
                k: row[k] for k in ("Coverage", "BranchCoverage", "LineCoverage", "Total_Goals",
                                   "Covered_Goals") if k in row
            }

    out = os.path.join(base, "generation.json")
    with open(out, "w") as f:
        json.dump({"project": args.project, "side": args.side, "budget": args.budget,
                   "classes": results}, f, indent=2)

    made = sum(1 for r in results if r["status"] in ("generated", "reused"))
    tests = sum(r.get("tests", 0) for r in results)
    bad = [r["class"] for r in results if r["status"] not in ("generated", "reused")]
    print(f"\n{made}/{len(classes)} suites, {tests} tests total  ->  {out}")
    if bad:
        print(f"no suite for {len(bad)}: " + ", ".join(c.split(".")[-1] for c in bad[:8])
              + (" ..." if len(bad) > 8 else ""))
    return 0 if made else 1


if __name__ == "__main__":
    sys.exit(main())
