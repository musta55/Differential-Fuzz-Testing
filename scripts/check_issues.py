#!/usr/bin/env python3
"""
Regression suite for the reported bugs, run end to end through the real pipeline.

    python3 scripts/check_issues.py [--duration 15s] [--case NAME] [--keep]

Each case under examples/issues/ is a minimal original/refactored pair with a known correct
outcome. The script runs build -> prune -> harnesses -> fuzz for each and asserts both the number
of methods the differ found and the verdict the fuzzer reached, then prints PASS/FAIL per case.

WHY IT ASSERTS TWO DIFFERENT THINGS
The three bugs failed at two different stages. #1 and #2 reached the fuzzer and got the wrong
verdict; #3 never reached it at all, because the method was not recognised as changed and so no
harness was ever generated. A suite that only checked verdicts would have scored #3 as "no
failures" — the same way the tool originally reported "0 changed methods" and looked healthy.

The guard-* cases exist so the fixes cannot be "passed" by over-reporting: blanking fewer things
must not make a comment edit look like a change, and comparing cause chains must not make two
identical throws look different.

This uses the example profile's Dataset/harness directories, so it OVERWRITES whatever project was
built there last. Rebuild your project afterwards (or pass --keep to skip the final cleanup note).
"""
import argparse
import json
import os
import re
import subprocess
import sys
import time

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
CASES_DIR = os.path.join(MODULE, "examples/issues")

# name -> (github issue, expected method count, expected verdict, what it proves)
CASES = {
    "issue1-wrapped-exception": (
        1, 1, "DIVERGENT",
        "same outer Error, different wrapped cause -> must not be EQUIVALENT"),
    "issue2-empty-list": (
        2, 1, "DIVERGENT",
        "List<Integer> parameter must be populated, not empty"),
    "issue3-string-literal": (
        3, 1, "DIVERGENT",
        "a method whose only change is a string literal must still be found and fuzzed"),
    "guard-comment-only": (
        None, 0, None,
        "a comment-only edit must still count as unchanged"),
    "guard-same-cause": (
        None, 1, "EQUIVALENT",
        "identical cause chains must stay EQUIVALENT (no false positive from the #1 fix)"),
}


def run(argv, **kw):
    return subprocess.run(argv, cwd=MODULE, capture_output=True, text=True, **kw)


def build(case):
    """Returns the number of changed methods the differ found."""
    root = os.path.join(CASES_DIR, case)
    # target/seeds/example too: it holds whatever project was seeded last (the demo, typically),
    # whose seeds belong to harnesses these fixtures do not have. Jazzer would ignore them, but the
    # report header would still claim a seed corpus that had nothing to do with the run.
    for stale in ("src/test/Dataset/example", "src/test/resources/example",
                  "src/test/fuzzing/example", "src/test/resources/fuzz",
                  "target/seeds/example"):
        subprocess.run(["rm", "-rf", os.path.join(MODULE, stale)], cwd=MODULE)
    r = run([sys.executable, os.path.join(MODULE, "scripts/build_project.py"), "example",
             "--original", os.path.join(root, "original"),
             "--refactored", os.path.join(root, "refactored")])
    m = re.search(r"(\d+) classes, (\d+) changed methods", r.stdout)
    if not m:
        print(r.stdout, r.stderr)
        return None
    return int(m.group(2))


REPORT_DIR = os.path.join(MODULE, "reports", "issues")


def fuzz(duration, case):
    """Run the pipeline for the already-built example project; returns {method id: verdict}.

    Each case writes its own report. They all build into the `example` profile, so a shared report
    name would leave only the last case's file behind — which is exactly what happened before.
    """
    run([sys.executable, os.path.join(MODULE, "scripts/prune.py"), "example"])
    run([sys.executable, os.path.join(MODULE, "scripts/gen_harnesses.py"), "example", duration])
    r = run([sys.executable, os.path.join(MODULE, "scripts/run_project.py"), "example",
             "--report-dir", REPORT_DIR, "--report", case + ".md"])
    verdicts = {}
    for line in r.stdout.splitlines():
        m = re.match(r"\s*\[\d+/\d+\]\s+(\S+)\s+\([^)]*\)\s+\.\.\.\s+(\S+)", line)
        if m:
            verdicts[m.group(1)] = m.group(2)
    if not verdicts:
        print(r.stdout[-1500:], r.stderr[-500:])
    return verdicts


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--duration", default="15s", help="fuzz budget per method (default 15s)")
    ap.add_argument("--case", help="run a single case by directory name")
    args = ap.parse_args()

    names = [args.case] if args.case else list(CASES)
    failures = []
    results = {}
    print(f"Running {len(names)} regression case(s) at {args.duration}/method\n")

    for name in names:
        issue, want_methods, want_verdict, what = CASES[name]
        label = f"issue #{issue}" if issue else "guard"
        print(f"── {name}  ({label})\n   {what}")

        results[name] = {"issue": issue, "what": what, "want_methods": want_methods,
                         "want_verdict": want_verdict, "methods": None, "verdict": None,
                         "pass": False}
        got_methods = build(name)
        if got_methods is None:
            failures.append(f"{name}: build_project.py produced no parseable output")
            print("   FAIL — build_project.py failed\n")
            continue
        results[name]["methods"] = got_methods
        ok = got_methods == want_methods
        print(f"   methods found: {got_methods} (want {want_methods}) "
              f"{'OK' if ok else 'MISMATCH'}")
        if not ok:
            failures.append(f"{name}: found {got_methods} changed methods, expected {want_methods}")
            print("   FAIL\n")
            continue
        if want_verdict is None:
            results[name]["pass"] = True
            print("   PASS\n")
            continue

        verdicts = fuzz(args.duration, name)
        if not verdicts:
            failures.append(f"{name}: the fuzz run produced no verdict")
            print("   FAIL — no verdict\n")
            continue
        got = list(verdicts.values())[0]
        mid = list(verdicts.keys())[0]
        results[name]["verdict"] = got
        if got == want_verdict:
            results[name]["pass"] = True
            print(f"   verdict: {mid} -> {got} (want {want_verdict}) OK")
            print("   PASS\n")
        else:
            failures.append(f"{name}: {mid} -> {got}, expected {want_verdict}")
            print(f"   verdict: {mid} -> {got} (want {want_verdict}) MISMATCH")
            print("   FAIL\n")

    write_summary(names, results, args.duration)

    print("=" * 70)
    if failures:
        print(f"{len(failures)} FAILURE(S):")
        for f in failures:
            print("  -", f)
    else:
        print(f"all {len(names)} cases PASSED")
    print(f"reports: {os.path.relpath(REPORT_DIR, MODULE)}/  "
          f"(one per case, plus SUMMARY.md)")
    print("Note: this overwrote src/test/Dataset/example — rebuild your project before fuzzing it.")
    return 1 if failures else 0


def write_summary(names, results, duration):
    """Index the per-case reports and state, per case, whether the bug is fixed.

    The per-case report answers "what did the fuzzer conclude"; only this file answers "is the
    reported bug gone", because that needs the EXPECTED outcome alongside the observed one.
    """
    os.makedirs(REPORT_DIR, exist_ok=True)
    path = os.path.join(REPORT_DIR, "SUMMARY.md")
    passed = [n for n in names if results[n]["pass"]]
    with open(path, "w") as f:
        f.write("# Reported-bug regression suite\n\n")
        f.write(f"Generated {time.strftime('%Y-%m-%d %H:%M:%S')} · "
                f"**{duration}** per method · {len(passed)}/{len(names)} cases passing\n\n")
        f.write("Each case is a minimal `original`/`refactored` pair in `examples/issues/`, run "
                "through the full pipeline. Two things are asserted per case — how many changed "
                "methods the differ found, and the verdict the fuzzer reached — because the bugs "
                "failed at two different stages: some produced a wrong verdict, one produced no "
                "verdict at all because the method was never recognised as changed.\n\n")
        f.write("These cases run **unseeded** — they are behavioural assertions, not a seeding "
                "benchmark, and each is small enough that the fuzzer finds the witness in seconds. "
                "The seed corpus is cleared first so a report cannot credit seeds it never used.\n\n")
        f.write("| Case | Issue | Methods found | Verdict | Expected | Result | Report |\n")
        f.write("|---|---|---:|---|---|---|---|\n")
        for n in names:
            r = results[n]
            issue = f"[#{r['issue']}](https://github.com/musta55/Differential-Fuzz-Testing/issues/{r['issue']})" \
                if r["issue"] else "guard"
            want = f"{r['want_methods']} method(s)" + (f", {r['want_verdict']}"
                                                       if r["want_verdict"] else ", not fuzzed")
            link = f"[{n}.md]({n}.md)" if r["want_verdict"] else "— (no method to fuzz)"
            f.write(f"| `{n}` | {issue} | {r['methods']} | {r['verdict'] or '—'} | {want} "
                    f"| {'**PASS**' if r['pass'] else '**FAIL**'} | {link} |\n")
        f.write("\n## What each case proves\n\n")
        for n in names:
            f.write(f"- **`{n}`** — {results[n]['what']}\n")
        f.write("\n## Reproduce\n\n```bash\npython3 scripts/check_issues.py\n"
                "python3 scripts/check_issues.py --case issue1-wrapped-exception\n```\n")
    print(f"\nsummary -> {os.path.relpath(path, MODULE)}")


if __name__ == "__main__":
    sys.exit(main())
