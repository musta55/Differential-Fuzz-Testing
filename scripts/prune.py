#!/usr/bin/env python3
"""
Iteratively `./mvnw -P<project> test-compile` and drop any <Class>{Original,Refactored} pair that
fails to compile (missing project deps), plus its manifest entries, until the build is clean.

This is the per-class dependency gate, automated: classes whose deps aren't on the
classpath are skipped (add the dep to the profile later to recover them).

It also enforces the two preconditions that used to fail SILENTLY, because Maven treats an
unknown -P as a warning and exits 0:

  * the profile must exist in pom.xml — otherwise `-P<project>` selects nothing, the
    generated test-source roots are never added, `test-compile` compiles zero snapshots and
    still prints BUILD SUCCESS. Every later step then ran against classes that were never
    built, and all 167 methods came out as `harness error` SKIPs with nothing saying why.
  * something must actually come out of it — a clean compile that produced no snapshot
    .class files is the same non-event wearing a success message.

Usage: prune.py <project> [maxRounds=30]
"""
import json
import os
import re
import subprocess
import sys

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))


def failing_class_files(output):
    """Return {(dir, classBase)} for every <ClassBase>{Original,Refactored}.java in errors."""
    out = set()
    for m in re.finditer(r"(/\S+?)/(\w+?)(Original|Refactored)\.java:\[", output):
        out.add((m.group(1), m.group(2)))
    return out


def manifest_path(project):
    return os.path.join(MODULE, "src/test/resources", project, "manifest.json")


def require_profile(project):
    """A missing profile is the single most common setup mistake, and Maven will not report it."""
    pom = open(os.path.join(MODULE, "pom.xml"), encoding="utf-8").read()
    if re.search(rf"<id>\s*{re.escape(project)}\s*</id>", pom):
        return
    sys.exit(
        f"pom.xml has no <profile><id>{project}</id> — `./mvnw -P{project}` would select nothing\n"
        f"and still exit 0, compiling none of the snapshots.\n\n"
        f"  Register the project first:\n"
        f"    python3 scripts/project_setup.py {project} -d <a checkout of the target project>\n")


def compiled_snapshots(project):
    """How many <Class>{Original,Refactored}.class the last compile actually produced."""
    man = json.load(open(manifest_path(project)))
    wanted = set()
    for e in man["methods"]:
        for side in ("original", "refactored"):
            wanted.add(e[side].replace(".", "/") + ".class")
    classes = os.path.join(MODULE, "target/test-classes")
    return sum(1 for rel in wanted if os.path.isfile(os.path.join(classes, rel))), len(wanted)


def filter_manifest(project, removed_bases):
    path = manifest_path(project)
    data = json.load(open(path))
    before = len(data["methods"])
    kept = []
    for e in data["methods"]:
        base = e["refactored"].split(".")[-1][:-len("Refactored")]
        if base not in removed_bases:
            kept.append(e)
    data["methods"] = kept
    json.dump(data, open(path, "w"), indent=2)
    return before - len(kept)


def main(project, max_rounds):
    require_profile(project)
    if not os.path.isfile(manifest_path(project)):
        sys.exit(f"no manifest for '{project}' — run scripts/build_project.py first")

    all_removed = set()
    for rnd in range(max_rounds):
        p = subprocess.run(["./mvnw", f"-P{project}", "test-compile"], cwd=MODULE, capture_output=True, text=True)
        out = p.stdout + p.stderr
        if "BUILD SUCCESS" in out:
            built, wanted = compiled_snapshots(project)
            if wanted and built == 0:
                sys.exit(
                    f"\ncompile reported SUCCESS but produced 0 of {wanted} snapshot classes.\n"
                    f"The -P{project} profile is not adding src/test/Dataset/{project} as a test-source\n"
                    f"root. Re-register the project:\n"
                    f"    python3 scripts/project_setup.py {project} -d <target project checkout>\n")
            print(f"compile clean after {rnd} prune round(s). pruned classes: {len(all_removed)}")
            print(f"  snapshots compiled: {built}/{wanted}")
            if all_removed:
                print("  pruned:", " ".join(sorted(all_removed)))
            return 0
        fails = failing_class_files(out)
        if not fails:
            print("BUILD FAILURE but no <Class>{Original,Refactored} file in errors — manual look:")
            print("\n".join(l for l in out.splitlines() if "ERROR" in l)[:2000])
            return 1
        bases = set()
        for d, base in fails:
            for suf in ("Original", "Refactored"):
                f = os.path.join(d, base + suf + ".java")
                if os.path.exists(f):
                    os.remove(f)
            bases.add(base)
        dropped = filter_manifest(project, bases)
        all_removed |= bases
        print(f"  round {rnd}: removed {len(bases)} class(es), {dropped} manifest entries -> {sorted(bases)}")
    print("hit max rounds; still failing")
    return 1


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(__doc__)
        sys.exit(2)
    sys.exit(main(sys.argv[1], int(sys.argv[2]) if len(sys.argv) > 2 else 30))
