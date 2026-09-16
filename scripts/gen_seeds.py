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
import glob
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


def _record(base, args, source, suites):
    """Leave the starting corpus in the workspace for connector/collect.py to archive."""
    with open(os.path.join(base, "seed-source.json"), "w") as f:
        json.dump({"project": args.project, "side": args.side, "source": source,
                   "suites": suites}, f, indent=2)


def generation_skipped(base):
    """The reason gen_unittests.py gave for not running EvoSuite at all, or None if it did run."""
    try:
        with open(os.path.join(base, "generation.json"), encoding="utf-8") as f:
            gen = json.load(f)
    except (OSError, ValueError):
        return None
    return gen.get("reason", "no reason recorded") if gen.get("status") == "skipped" else None


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("project")
    ap.add_argument("--side", default="original", choices=("original", "refactored"))
    ap.add_argument("--source-seeds", action="store_true",
                    help="mine seeds from the snapshot sources instead of using EvoSuite's; the "
                         "only way to seed a project EvoSuite cannot run on")
    args = ap.parse_args()

    ensure_compiled(args.project)
    base = os.path.join(MODULE, "target/evosuite", args.project)

    # EvoSuite cannot run against a project built past Java 8, and an unseeded fuzzer starts from
    # nothing. Mining the snapshot source for constants is the JDK-independent fallback; it produces
    # the same seed-values.json, so nothing below changes.
    #
    # The fallback triggers only when gen_unittests.py RECORDED that it skipped, never merely
    # because no suites are on disk. "No suites" is ambiguous — it also means "gen_unittests has not
    # run yet" and "the suites are parked" (jmeter's 283 sit in evosuite-tests.disabled while an
    # unseeded baseline is measured). Falling back on those would silently swap a project's seed
    # source between runs, which is precisely the comparison this records exist to protect.
    suites = glob.glob(os.path.join(base, "evosuite-tests", "**", "*_ESTest.java"), recursive=True)
    skipped = generation_skipped(base)
    if args.source_seeds:
        source, script = "source-literals", "scripts/source_seeds.py"
        print(f"mining seeds from the {args.project} snapshot sources (requested)")
    elif suites:
        source, script = "evosuite", "scripts/extract_seeds.py"
    elif skipped:
        # A project EvoSuite cannot run on fuzzes UNSEEDED by default, and that is a clean outcome,
        # not an error: run_project.py starts the fuzzer with an empty corpus and says so. Mining
        # the sources instead is available but opt-in (--source-seeds), because silently swapping
        # one project's starting corpus for a different kind would make its divergence rate
        # incomparable to the projects EvoSuite did run on.
        print(f"no seed corpus for {args.project}: EvoSuite was skipped ({skipped})")
        print("  the fuzzer will start unseeded; pass --source-seeds to mine the sources instead")
        _record(base, args, "none", 0)
        return 0
    else:
        sys.exit(f"no suites for {args.project} — run scripts/gen_unittests.py first "
                 f"(or pass --source-seeds to mine them from the sources)")

    r = subprocess.run([sys.executable, os.path.join(MODULE, script),
                        args.project, "--side", args.side], cwd=MODULE)
    if r.returncode != 0:
        return r.returncode

    _record(base, args, source, len(suites))

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
    print(f"staged {n} seed files in {os.path.relpath(staging, MODULE)} (source: {source})")
    return 0


if __name__ == "__main__":
    sys.exit(main())
