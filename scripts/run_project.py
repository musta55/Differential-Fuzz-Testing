#!/usr/bin/env python3
"""
Run the differential-fuzzing suite for one project, driven by its manifest.json.

For each method: JAZZER_FUZZ=1 mvn test on its generated harness (1 min via maxDuration),
classify EQUIVALENT / DIVERGENT / ERROR, measure per-method JaCoCo branch/line coverage,
and write reports/<project>/auto-fuzz-report.md (same columns as before).

A one-sided watchdog TIMEOUT is treated as inconclusive by the engine (not divergent).

Usage: run_project.py <project> [--max N] [--regression]
"""
import glob 
import json
import os
import re
import subprocess
import sys
import time

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))


def cov_classpath():
    core = glob.glob(os.path.expanduser("~/.m2/repository/org/jacoco/org.jacoco.core/0.8.11/org.jacoco.core-0.8.11.jar"))
    asm = os.path.expanduser("~/.m2/repository/org/ow2/asm")
    jars = [f"{MODULE}/target/covtool"]
    if core:
        jars.append(core[0])
    for a in ("asm/9.6/asm-9.6.jar", "asm-commons/9.6/asm-commons-9.6.jar",
              "asm-tree/9.6/asm-tree-9.6.jar", "asm-analysis/9.6/asm-analysis-9.6.jar"):
        jars.append(os.path.join(asm, a))
    return ":".join(jars)


def harness_duration(project):
    """Read the maxDuration the harnesses were generated with (for the report header)."""
    projkey = project.replace("-", "_")
    files = glob.glob(os.path.join(MODULE, "src/test/fuzzing", project, "fuzz/auto", projkey, "*FuzzTest.java"))
    if files:
        mm = re.search(r'maxDuration\s*=\s*"([^"]+)"', open(files[0]).read())
        if mm:
            return mm.group(1)
    return "?"


def constructible(entry, project):
    """Cheap pre-skip: only skip instance methods the engine clearly can't build a receiver for
    (abstract/interface, or only private constructors). Everything else runs — the engine now
    constructs scalar AND recursively-buildable object constructors, and fast-skips at runtime
    if it truly can't. Bias: prefer running (completeness) over pre-skipping."""
    if entry["static"]:
        return True
    fqn = entry["refactored"]
    path = os.path.join(MODULE, "src/test/Dataset", project, fqn.replace(".", "/") + ".java")
    if not os.path.isfile(path):
        return False
    src = open(path, errors="replace").read()
    simple = fqn.split(".")[-1]
    if re.search(r"\b(?:abstract\s+class|interface)\s+" + re.escape(simple) + r"\b", src):
        return False
    all_ctors = re.findall(r"(?:public|protected|private)\s+" + re.escape(simple) + r"\s*\(", src)
    pub_ctors = re.findall(r"(?:public|protected)\s+" + re.escape(simple) + r"\s*\(", src)
    if not all_ctors:
        return True  # implicit no-arg constructor
    return len(pub_ctors) > 0  # has an accessible constructor → let the engine try


def coverage(cp, refsimple, method):
    exe = os.path.join(MODULE, "target/jacoco.exec")
    if not os.path.exists(exe):
        return "n/a", "n/a"
    p = subprocess.run(["java", "-cp", cp, "CovReport", exe, f"{MODULE}/target/test-classes", refsimple, method],
                       capture_output=True, text=True)
    line = p.stdout.strip().splitlines()[0] if p.stdout.strip() else ""
    b = re.search(r"branch=(\d+/\d+)", line)
    l = re.search(r"line=(\d+/\d+)", line)
    return (b.group(1) if b else "n/a"), (l.group(1) if l else "n/a")


def main(project, max_n, mode):
    projkey = project.replace("-", "_")
    man = json.load(open(os.path.join(MODULE, "src/test/resources", project, "manifest.json")))
    entries = man["methods"][:max_n] if max_n else man["methods"]
    dur = harness_duration(project)
    cp = cov_classpath()
    os.makedirs(f"{MODULE}/target/covtool", exist_ok=True)
    subprocess.run(["javac", "-cp", cp.split(":")[1] if ":" in cp else cp, "-d", f"{MODULE}/target/covtool",
                    f"{MODULE}/scripts/CovReport.java"], capture_output=True)

    logdir = os.path.join(MODULE, "target/fuzz-logs", project)
    os.makedirs(logdir, exist_ok=True)
    repdir = os.path.join(MODULE, "reports", project)
    os.makedirs(repdir, exist_ok=True)
    report = os.path.join(repdir, "auto-fuzz-report.md")

    env = dict(os.environ)
    if mode == "fuzz":
        env["JAZZER_FUZZ"] = "1"
    else:
        env.pop("JAZZER_FUZZ", None)

    with open(report, "w") as r:
        r.write(f"# Differential-Fuzzing Report — {project} ({man.get('model','qwen')})\n\n")
        r.write(f"- Generated: {time.strftime('%Y-%m-%d %H:%M:%S')}  ·  Mode: **{mode}**  ·  {dur}/method\n")
        r.write(f"- {len(entries)} auto-fuzzable methods (manifest-driven; original=<Class>Original, refactored=<Class>Refactored).\n")
        r.write("- Branch/Line = JaCoCo per-method coverage. DIVERGENT = exception-type or return-value mismatch (one-sided TIMEOUT is NOT counted).\n\n")
        r.write("| Method | Result | Tests (fail) | Branch | Line | Evidence |\n")
        r.write("|--------|--------|--------------|--------|------|----------|\n")

    eq = div = err = skip = find = 0
    for i, e in enumerate(entries, 1):
        harness = f"fuzz.auto.{projkey}.Auto_" + e["id"].replace(".", "_") + "_FuzzTest"
        log = os.path.join(logdir, e["id"].replace(".", "_") + ".log")
        refsimple = e["refactored"].split(".")[-1]
        print(f"  [{i}/{len(entries)}] {e['id']} ...", end=" ", flush=True)
        # pre-skip instance methods with no no-arg ctor (don't burn a 1-min fuzz on a no-op)
        if not constructible(e, project):
            skip += 1
            print("SKIP (no no-arg ctor)")
            with open(report, "a") as r:
                r.write(f"| {e['id']} | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |\n")
            continue
        exe = os.path.join(MODULE, "target/jacoco.exec")
        if os.path.exists(exe):
            os.remove(exe)
        with open(log, "w") as lf:
            subprocess.run(["timeout", "240", "mvn", f"-P{project}", "test", f"-Dtest={harness}",
                            "-Dsurefire.failIfNoSpecifiedTests=false", "-Dmaven.test.failure.ignore=true"],
                           cwd=MODULE, env=env, stdout=lf, stderr=subprocess.STDOUT)
        out = open(log, errors="replace").read()
        m = re.search(r"Tests run: (\d+), Failures: (\d+), Errors: (\d+)", out)
        runs = m.group(1) if m else "?"
        fe = (int(m.group(2)) + int(m.group(3))) if m else 0
        branch, line = coverage(cp, refsimple, e["method"])
        if "DIFFERENTIAL MISMATCH" in out:
            div += 1
            om = re.search(r"original  : (.+)", out)
            rm = re.search(r"refactored: (.+)", out)
            res = "DIVERGENT"
            ev = (f"orig: {om.group(1).strip()[:30]} vs ref: {rm.group(1).strip()[:30]}"
                  if om and rm else "mismatch (see log)")
        elif "[SKIP]" in out:
            res, ev = "SKIP", "no no-arg ctor (instance)"
            skip += 1
        elif "com.code_intelligence.jazzer.api.FuzzerSecurityIssue" in out:
            # A Jazzer sanitizer fired (reflective call / SSRF / ReDoS ...). The method DID run on
            # both sides and did not diverge — this is a finding about the code, not a harness
            # failure, so it must not be lumped in with ERROR.
            sm = sorted(set(re.findall(r"FuzzerSecurityIssue(Critical|High|Medium|Low)", out)))
            tm = re.search(r"FuzzerSecurityIssue\w+:\s*\n(.+)", out)
            res, find = "FINDING", find + 1
            ev = f"Jazzer sanitizer ({'/'.join(sm) or '?'}): {tm.group(1).strip()[:40] if tm else 'see log'}"
        elif m and fe == 0:
            res, ev, eq = "EQUIVALENT", f"no divergence in {dur}", eq + 1
        else:
            res, err = "ERROR", err + 1
            em = re.search(r"(NoSuchMethodException|NoClassDefFoundError|ClassNotFoundException|InstantiationException)", out)
            ev = (em.group(1) if em else "harness error") + " (skip)"
        print(f"{res}  (branch {branch})")
        with open(report, "a") as r:
            r.write(f"| {e['id']} | **{res}** | {runs} ({fe}) | {branch} | {line} | {ev} |\n")

    with open(report, "a") as r:
        r.write(f"\n## Summary\n\n- EQUIVALENT **{eq}** · DIVERGENT **{div}** · FINDING **{find}** · SKIP **{skip}** · ERROR **{err}**"
                f"  ({len(entries)} methods)\n")
        r.write("- SKIP = instance method whose receiver can't be built generically (abstract/interface, or no synthesizable constructor).\n")
        r.write("- FINDING = ran on both sides without diverging, but a Jazzer sanitizer fired on the code itself.\n")
        r.write("- ERROR = could not be tested (missing class at runtime, inaccessible member, bad manifest entry).\n")
    print(f"Done. EQUIVALENT={eq} DIVERGENT={div} FINDING={find} SKIP={skip} ERROR={err}  ->  {report}")


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(__doc__)
        sys.exit(2)
    args = sys.argv[1:]
    proj = args[0]
    mx = int(args[args.index("--max") + 1]) if "--max" in args else 0
    md = "regression" if "--regression" in args else "fuzz"
    main(proj, mx, md)
