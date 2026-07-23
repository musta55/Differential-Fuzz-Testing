#!/usr/bin/env python3
"""
Differential "first tester" — one entry point: two source trees in, one report out.

    python3 run.py <project> --original <origTree> --refactored <refTree> [options]

It chains the four pipeline steps (each is a standalone script under scripts/):

    1. build_project.py   diff original vs refactored -> manifest.json + <Class>{Original,Refactored} snapshots
    2. prune.py           iterative `mvn test-compile`; drop pairs whose deps don't resolve
    3. gen_harnesses.py   one thin Jazzer harness per manifest method
    4. run_project.py     fuzz each method + JaCoCo coverage -> reports/<project>/auto-fuzz-report.md

Requires Maven + a JDK for steps 2-4 (the fuzzing itself). Step 1 is pure Python.
The target project's own dependencies must be resolvable (for apex-core: run
scripts/setup_deps.sh first, or add them to the -P<project> profile in pom.xml).

Example (apex-core, from the bundled example trees):
    scripts/setup_deps.sh /path/to/built/apex-core
    python3 run.py apex-core --original examples/apex-core/original \\
                             --refactored examples/apex-core/refactored
"""
import argparse
import os
import subprocess
import sys

MODULE = os.path.dirname(os.path.abspath(__file__))
SCRIPTS = os.path.join(MODULE, "scripts")


def step(title, argv):
    print(f"\n=== {title} ===\n  $ {' '.join(argv)}", flush=True)
    r = subprocess.run(argv, cwd=MODULE)
    if r.returncode != 0:
        print(f"\nFAILED at: {title} (exit {r.returncode})", file=sys.stderr)
        sys.exit(r.returncode)


def py(script, *args):
    return [sys.executable, os.path.join(SCRIPTS, script), *args]


def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("project", help="profile id in pom.xml (e.g. apex-core)")
    ap.add_argument("--original", required=True, help="original source tree root")
    ap.add_argument("--refactored", required=True, help="refactored source tree root")
    ap.add_argument("--duration", default="1m", help="Jazzer maxDuration per method (default 1m)")
    ap.add_argument("--max", type=int, default=0, help="only run the first N methods (smoke test)")
    ap.add_argument("--skip-build", action="store_true", help="reuse existing manifest/snapshots")
    ap.add_argument("--skip-prune", action="store_true", help="skip the compile-and-drop gate")
    args = ap.parse_args()

    if not args.skip_build:
        step("1/4 build manifest + snapshots",
             py("build_project.py", args.project, "--original", args.original, "--refactored", args.refactored))
    if not args.skip_prune:
        step("2/4 prune uncompilable pairs", py("prune.py", args.project))
    step("3/4 generate harnesses", py("gen_harnesses.py", args.project, args.duration))
    run_argv = py("run_project.py", args.project)
    if args.max:
        run_argv += ["--max", str(args.max)]
    step("4/4 fuzz + coverage -> report", run_argv)

    report = os.path.join(MODULE, "reports", args.project, "auto-fuzz-report.md")
    print(f"\nDONE. Report: {report}")


if __name__ == "__main__":
    main()
