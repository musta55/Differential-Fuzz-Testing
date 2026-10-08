#!/usr/bin/env python3
"""
Differential "first tester" — one entry point: two source trees in, one report out.

    python3 run.py <project> [--original <origTree> --refactored <refTree>] [options]

It chains the pipeline steps (each is a standalone script under scripts/):

    1. build_project_manifest.py   diff original vs refactored -> manifest.json of changed methods
    2. compile_sides.py   compile each side under its real names (gates 1 + 2) -> target/sides/
    3. gen_harnesses.py   one thin Jazzer harness per manifest method
    4. gen_unittests.py   EvoSuite unit suites for the original classes
    5. gen_seeds.py       those tests' constants, encoded as the fuzzer's seed corpus
    6. run_project.py     fuzz each method + differential coverage -> reports/<project>/auto-fuzz-report.md

The unit suite is not an optional extra: it is where the fuzzer's starting inputs come from, so
libFuzzer begins from values that already reach the code instead of growing them from random bytes.
Coverage is Jazzer's own and is reported for BOTH sides — there is no JaCoCo agent here.

Requires Maven + a JDK for steps 2-6, and scripts/setup_evosuite.sh for steps 4-5 (which also need
a Java 8 JVM — see JAVA8_HOME). Step 1 is pure Python.

STEP 0, once per project: scripts/project_setup.py builds the target project, shades its modules
and their dependencies into one jar, writes the matching -P<project> profile into pom.xml and
records the two trees in projects.json. After that --original/--refactored are optional here,
because they come from the registry.

    python3 scripts/project_setup.py deltaspike \\
        --original projects/before/deltaspike --refactored projects/after/deltaspike
    python3 run.py deltaspike

The bundled demos need no registration:
    python3 run.py example
    python3 run.py apex-core --max 5
"""
import argparse
import os
import subprocess
import sys

MODULE = os.path.dirname(os.path.abspath(__file__))
SCRIPTS = os.path.join(MODULE, "scripts")
sys.path.insert(0, SCRIPTS)
import projects


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
    ap.add_argument("project", help=" project name (e.g., deltaspike)")
    ap.add_argument("--original", help="/path/to/original/project", required=True)
    ap.add_argument("--refactored", help="/path/to/refactored/project")
    ap.add_argument("--setup", help="step 0: build the project and register it before running.", default=True)
    ap.add_argument("--build-args", metavar="ARGS",
                    help="with --setup: extra arguments for the project's own build")
    ap.add_argument("--duration", default="1m", help="Jazzer maxDuration per method (default 1m)")
    ap.add_argument("--max", type=int, default=0,
                    help="smoke test: only the first N methods, and only the classes they live in "
                         "(this bound reaches step 4 too — generating EvoSuite suites for all of a "
                         "big project's classes used to dominate the runtime of a 5-method run)")
    ap.add_argument("--jobs", type=int, default=75,
                    help="EvoSuite classes, and step 6's fuzzed methods, to run in parallel (default 75)")
    ap.add_argument("--skip-build", action="store_true", help="reuse the existing manifest")
    ap.add_argument("--skip-compile", action="store_true", help="reuse the compiled sides")
    ap.add_argument("--skip-unittests", action="store_true",
                    help="reuse the existing EvoSuite suites instead of regenerating them")
    ap.add_argument("--unit-budget", type=int, default=60,
                    help="EvoSuite search seconds per class (default 60)")
    ap.add_argument("--source-seeds", action="store_true",
                    help="mine the seed corpus from the project sources instead of from EvoSuite. "
                         "The only way to seed a project built past Java 8: EvoSuite 1.2.0 runs on "
                         "a Java 8 JVM, which cannot load newer classes, so step 4 skips itself "
                         "and the fuzzer would otherwise start from an empty corpus")
    ap.add_argument("--report", default="auto-fuzz-report.md", help="report filename")
    args = ap.parse_args()

    if args.setup is not None:
        if not args.setup and not args.original:
            ap.error("--setup with no DIR builds the --original tree, so --original is required")
        setup = py("project_setup.py", args.project)
        for flag in ("original", "refactored", "build_args"):
            if getattr(args, flag):
                # setup += ["--" + flag.replace("_", "-"), getattr(args, flag)]
                setup += [f"--{flag.replace('_', '-')}={getattr(args, flag)}"]
        step("0/6 build the target project + register its profile", setup)

    # Explicit trees win; otherwise the registry supplies them, and says how to register the
    # project if it is not there. Both used to be required flags, which meant every invocation
    # had to repeat two absolute paths that never change for a given project.
    original, refactored = projects.resolve_trees(args.project, args.original, args.refactored)

    if not args.skip_build:
        step("1/6 build the manifest.json of changed methods",
        py("build_project_manifest.py", args.project, "--original", original, "--refactored", refactored))
    
    if not args.skip_compile:
        step("2/6 compile original and refactored classes", py("compile_sides.py", args.project))
    
    step("3/6 generate harnesses (Auto Fuzz tests)", py("gen_harnesses.py", args.project, args.duration))
    
    if not args.skip_unittests: ## default args.skip_unittests=False
        unit_argv = py("gen_unittests.py", args.project, "--budget", str(args.unit_budget),
                        "--jobs", str(args.jobs))
        if args.max:
            unit_argv += ["--max", str(args.max)]
        step("4/6 generate unit tests (EvoSuite)", unit_argv)
    
    seed_argv = py("gen_seeds.py", args.project) ## start checking code from here

    if args.source_seeds:
        seed_argv += ["--source-seeds"]
        print("Source Seeds Enabled")
    step("5/6 encode unit tests as fuzzer seeds", seed_argv)

    run_argv = py("run_project.py", args.project, "--report", args.report, "--jobs", str(args.jobs))
    if args.max:
        run_argv += ["--max", str(args.max)]
    step("6/6 fuzz + differential coverage -> report", run_argv)

    report = os.path.join(MODULE, "reports", args.project, args.report)
    print(f"\nDONE. Report: {report}")


if __name__ == "__main__":
    main()
