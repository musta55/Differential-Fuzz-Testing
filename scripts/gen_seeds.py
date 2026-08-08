#!/usr/bin/env python3
"""
Build the fuzzer's seed corpus from the generated unit tests.

    gen_seeds.py <project> [--side original]

Two steps: extract the constants of each EvoSuite test case (scripts/extract_seeds.py), then encode
them into Jazzer corpus files with fuzz.auto.SeedWriter, which drives the engine's own argument
builder against a recording provider and verifies every seed by decoding it back.

Seeds are staged in target/seeds/<project>/, NOT written straight into src/test/resources. The
staging copy is the master; scripts/run_project.py installs it for a seeded run and removes it for
an unseeded baseline, so switching between the two arms of a comparison cannot leave stale seeds
behind.

Prerequisite: scripts/gen_unittests.py <project>.
"""
import argparse
import json
import os
import subprocess
import sys

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))


def classpath(project):
    cache = os.path.join(MODULE, "target", f"cp-{project}.txt")
    if not os.path.isfile(cache):
        r = subprocess.run([os.path.join(MODULE, "mvnw"), f"-P{project}", "-q",
                            "dependency:build-classpath", f"-Dmdep.outputFile={cache}",
                            "-DincludeScope=test"], cwd=MODULE, capture_output=True, text=True)
        if not os.path.isfile(cache):
            sys.exit(f"could not resolve the {project} classpath:\n{r.stdout}\n{r.stderr}")
    return os.pathsep.join([os.path.join(MODULE, "target/test-classes"),
                            open(cache).read().strip()])


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
    args = ap.parse_args()

    ensure_compiled(args.project)
    base = os.path.join(MODULE, "target/evosuite", args.project)
    if not os.path.isdir(os.path.join(base, "evosuite-tests")):
        sys.exit(f"no suites for {args.project} — run scripts/gen_unittests.py first")

    r = subprocess.run([sys.executable, os.path.join(MODULE, "scripts/extract_seeds.py"),
                        args.project, "--side", args.side], cwd=MODULE)
    if r.returncode != 0:
        return r.returncode

    values = os.path.join(base, "seed-values.json")
    if not os.path.isfile(values) or not json.load(open(values)):
        print("no constants extracted; nothing to seed")
        return 0

    staging = os.path.join(MODULE, "target/seeds", args.project)
    # SeedWriter lays out <root>/fuzz/auto/<projkey>/... , the same shape the test-resources root
    # has, so staging can be copied verbatim onto src/test/resources at install time.
    r = subprocess.run(["java", "-cp", classpath(args.project), "fuzz.auto.SeedWriter",
                        args.project, values, staging], cwd=MODULE)
    if r.returncode != 0:
        return r.returncode
    n = sum(len(fs) for _, _, fs in os.walk(staging))
    print(f"staged {n} seed files in {os.path.relpath(staging, MODULE)}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
