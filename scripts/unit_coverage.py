#!/usr/bin/env python3
"""
Measure what the EvoSuite unit suites actually cover, per method, under JaCoCo.

    unit_coverage.py <project> [--side original]

Compiles target/evosuite/<project>/evosuite-tests/**_ESTest.java against the compiled snapshots,
runs them with JUnit 4 under the JaCoCo agent, and reports per-method branch/line coverage for
every method in the manifest -> target/evosuite/<project>/unit-coverage.json.

STANDALONE, AND THE ONLY PLACE A JACOCO AGENT IS STILL USED
This is NOT part of run.py's pipeline. The fuzzing report gets its coverage from Jazzer, which
measures only what the fuzzer drove; that mechanism cannot measure a plain JUnit run, because
Jazzer is not in the loop when the unit suite executes. So answering "how good is the seed suite
on its own?" still needs an agent, and this script uses the JaCoCo one. If that question does not
matter to you, this script can be deleted and nothing else changes.

WHY JACOCO AND NOT EVOSUITE'S OWN NUMBERS
EvoSuite reports its coverage in evosuite-report/statistics.csv, and that is read here too — but it
is per *class*, measured against EvoSuite's own instrumentation and its own goal set (a "branch"
there is a search goal, not a JaCoCo branch). The point of this step is a number directly comparable
to the fuzzer's, so the unit suite is measured with the same tool, the same class files and the same
per-method granularity that scripts/run_project.py reports for fuzzing. Both numbers are recorded;
only the JaCoCo one is comparable.

Requires scripts/setup_evosuite.sh (for the standalone runtime) and scripts/gen_unittests.py.
"""
import argparse
import csv
import glob
import json
import os
import re
import subprocess
import sys

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
M2 = os.path.expanduser("~/.m2/repository")

JUNIT4 = os.path.join(M2, "junit/junit/4.13.2/junit-4.13.2.jar")
HAMCREST = os.path.join(M2, "org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar")
JACOCO_AGENT = os.path.join(M2, "org/jacoco/org.jacoco.agent/0.8.11/org.jacoco.agent-0.8.11-runtime.jar")
EVO_RUNTIME = os.path.join(M2, "org/evosuite/local/evosuite-standalone-runtime/1.2.0-local/"
                               "evosuite-standalone-runtime-1.2.0-local.jar")


def cov_classpath():
    """Classpath for scripts/CovReport (jacoco core + asm)."""
    jars = [os.path.join(MODULE, "target/covtool"),
            os.path.join(M2, "org/jacoco/org.jacoco.core/0.8.11/org.jacoco.core-0.8.11.jar")]
    for a in ("asm/9.6/asm-9.6.jar", "asm-commons/9.6/asm-commons-9.6.jar",
              "asm-tree/9.6/asm-tree-9.6.jar", "asm-analysis/9.6/asm-analysis-9.6.jar"):
        jars.append(os.path.join(M2, "org/ow2/asm", a))
    return os.pathsep.join(jars)


def build_covtool():
    out = os.path.join(MODULE, "target/covtool")
    os.makedirs(out, exist_ok=True)
    core = os.path.join(M2, "org/jacoco/org.jacoco.core/0.8.11/org.jacoco.core-0.8.11.jar")
    subprocess.run(["javac", "-cp", core, "-d", out, os.path.join(MODULE, "scripts/CovReport.java")],
                   capture_output=True)


def project_classpath(project):
    cache = os.path.join(MODULE, "target", f"cp-{project}.txt")
    if not os.path.isfile(cache):
        subprocess.run([os.path.join(MODULE, "mvnw"), f"-P{project}", "-q",
                        "dependency:build-classpath", f"-Dmdep.outputFile={cache}",
                        "-DincludeScope=test"], cwd=MODULE, capture_output=True)
    deps = open(cache).read().strip() if os.path.isfile(cache) else ""
    classes = os.path.join(MODULE, "target/test-classes")
    return f"{classes}{os.pathsep}{deps}" if deps else classes


def per_method(exec_file, class_filter, method):
    cp = cov_classpath()
    p = subprocess.run(["java", "-cp", cp, "CovReport", exec_file,
                        os.path.join(MODULE, "target/test-classes"), class_filter, method],
                       capture_output=True, text=True)
    line = p.stdout.strip().splitlines()[0] if p.stdout.strip() else ""
    b = re.search(r"branch=(\d+/\d+)", line)
    l = re.search(r"line=(\d+/\d+)", line)
    return (b.group(1) if b else "n/a"), (l.group(1) if l else "n/a")


def evosuite_stats(base):
    """Merged from the per-class report dirs gen_unittests.py writes (one per parallel client)."""
    out = {}
    for path in glob.glob(os.path.join(base, "evosuite-report", "**", "statistics.csv"),
                          recursive=True):
        try:
            with open(path) as f:
                for row in csv.DictReader(f):
                    if row.get("TARGET_CLASS"):
                        out[row["TARGET_CLASS"]] = row
        except (OSError, csv.Error):
            continue
    return out


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
    ap.add_argument("--side", default="original", choices=("original", "refactored"))
    ap.add_argument("--suite-timeout", type=int, default=120,
                    help="seconds before one suite's JVM is killed (default 120)")
    args = ap.parse_args()

    base = os.path.join(MODULE, "target/evosuite", args.project)
    tests_dir = os.path.join(base, "evosuite-tests")
    if not os.path.isdir(tests_dir):
        sys.exit(f"no suites at {tests_dir} — run scripts/gen_unittests.py {args.project} first")
    for jar, what in ((JUNIT4, "junit 4.13.2"), (HAMCREST, "hamcrest-core 1.3"),
                      (JACOCO_AGENT, "jacoco agent 0.8.11"), (EVO_RUNTIME, "evosuite runtime")):
        if not os.path.isfile(jar):
            sys.exit(f"missing {what}: {jar}")

    ensure_compiled(args.project)
    build_covtool()
    sources = sorted(glob.glob(os.path.join(tests_dir, "**/*_ESTest*.java"), recursive=True))
    if not sources:
        sys.exit("no *_ESTest.java found")

    classes_out = os.path.join(base, "test-classes")
    os.makedirs(classes_out, exist_ok=True)
    cp = project_classpath(args.project)
    compile_cp = os.pathsep.join([cp, EVO_RUNTIME, JUNIT4, HAMCREST])

    # One javac per suite. Compiling them together lets a single bad file abort the batch: EvoSuite
    # occasionally emits a suite that does not compile (a generic it cannot spell, or a leftover
    # `executor` reference from the scaffolding it was told not to generate), and one such file cost
    # 21 of 25 apex-core suites in a single-invocation build. Isolating them keeps the loss to the
    # suite that is actually broken.
    print(f"compiling {len(sources)} suite files (one javac each)")
    suites, broken = [], []
    for s in sources:
        if not s.endswith("_ESTest.java"):
            continue
        rel = os.path.relpath(s, tests_dir)[:-len(".java")]
        r = subprocess.run(["javac", "-nowarn", "-cp", compile_cp, "-d", classes_out, s],
                           capture_output=True, text=True)
        if os.path.isfile(os.path.join(classes_out, rel + ".class")):
            suites.append(rel.replace(os.sep, "."))
        else:
            broken.append((os.path.basename(s), (r.stderr.splitlines() or [""])[0]))
    if broken:
        print(f"  {len(broken)} suites did not compile:")
        for name, why in broken[:5]:
            print(f"    {name}: {why.strip()[:100]}")
    if not suites:
        sys.exit("no suite compiled successfully")

    exec_file = os.path.join(base, "jacoco-unit.exec")
    if os.path.exists(exec_file):
        os.remove(exec_file)
    run_cp = os.pathsep.join([classes_out, cp, EVO_RUNTIME, JUNIT4, HAMCREST])
    work = os.path.join(base, "work")
    os.makedirs(work, exist_ok=True)

    # One JVM per suite, not one JVM for all of them. These are generated tests over server code
    # that opens sockets and starts threads, and a single non-terminating test blocked the whole
    # run past 30 minutes — taking every other suite's coverage down with it, since the agent only
    # writes the .exec at JVM exit. Per-suite runs bound the damage to the class that hangs, and
    # append=true keeps the one .exec accumulating across them.
    ran_total, failed_total, hung = 0, 0, []
    print(f"running {len(suites)} suites under JaCoCo (one JVM each, "
          f"{args.suite_timeout}s limit)")
    for fqn in suites:
        out = ""
        try:
            r = subprocess.run(
                ["java", "-Djava.awt.headless=true",
                 f"-javaagent:{JACOCO_AGENT}=destfile={exec_file},append=true",
                 "-cp", run_cp, "org.junit.runner.JUnitCore", fqn],
                capture_output=True, text=True, cwd=work, timeout=args.suite_timeout)
            out = r.stdout
        except subprocess.TimeoutExpired as e:
            hung.append(fqn)
            raw = e.stdout or ""
            out = raw.decode("utf-8", "replace") if isinstance(raw, bytes) else raw
        m = re.search(r"OK \((\d+) tests?\)", out) or re.search(r"Tests run: (\d+)", out)
        f = re.search(r"Failures: (\d+)", out)
        ran_total += int(m.group(1)) if m else 0
        failed_total += int(f.group(1)) if f else 0
    ran = ran_total
    print(f"  {ran_total} tests ran, {failed_total} failing"
          + (f"; {len(hung)} suites timed out ("
             + ", ".join(s.split(".")[-1] for s in hung[:4]) + ")" if hung else ""))
    if not os.path.exists(exec_file):
        sys.exit("jacoco produced no exec file (no suite started successfully)")

    man = json.load(open(os.path.join(MODULE, "src/test/resources", args.project, "manifest.json")))
    stats = evosuite_stats(base)
    rows = []
    for e in man["methods"]:
        simple = e[args.side].split(".")[-1]
        branch, line = per_method(exec_file, simple, e["method"])
        row = {"id": e["id"], "class": e[args.side], "method": e["method"],
               "branch": branch, "line": line}
        st = stats.get(e[args.side])
        if st:
            row["evosuite_reported"] = {k: st[k] for k in ("Coverage", "BranchCoverage",
                                                           "LineCoverage") if k in st}
        rows.append(row)

    out = os.path.join(base, "unit-coverage.json")
    with open(out, "w") as f:
        json.dump({"project": args.project, "side": args.side, "tests_run": ran,
                   "exec": exec_file, "methods": rows}, f, indent=2)

    covered = sum(1 for r_ in rows if r_["line"] not in ("n/a", "") and
                  r_["line"].split("/")[0] != "0")
    print(f"\nper-method coverage for {len(rows)} methods "
          f"({covered} with at least one line covered)  ->  {out}")
    for r_ in rows[:10]:
        print(f"  {r_['id']:<45} branch {r_['branch']:>7}  line {r_['line']:>7}")
    if len(rows) > 10:
        print(f"  ... and {len(rows) - 10} more")
    return 0


if __name__ == "__main__":
    sys.exit(main())
