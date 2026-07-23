#!/usr/bin/env python3
"""
Iteratively `mvn test-compile` and drop any <Class>{Original,Refactored} pair that fails to
compile (missing project deps), plus its manifest entries, until the build is clean.

This is the per-class dependency gate, automated: classes whose deps aren't on the
classpath are skipped (add the dep to pom.xml later to recover them).

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


def filter_manifest(project, removed_bases):
    path = os.path.join(MODULE, "src/test/resources", project, "manifest.json")
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
    all_removed = set()
    for rnd in range(max_rounds):
        p = subprocess.run(["mvn", f"-P{project}", "test-compile"], cwd=MODULE, capture_output=True, text=True)
        out = p.stdout + p.stderr
        if "BUILD SUCCESS" in out:
            print(f"compile clean after {rnd} prune round(s). pruned classes: {len(all_removed)}")
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
