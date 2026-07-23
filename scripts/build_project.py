#!/usr/bin/env python3
"""
Build the manifest + compiled-source snapshots for a project from two parallel source trees.

INPUT CONTRACT (decoupled — no RefAgent/results layout assumed):
    build_project.py <project> --original <origTree> --refactored <refTree>

  <origTree> and <refTree> are two source trees with the SAME package structure, e.g.
      original/com/acme/Foo.java     refactored/com/acme/Foo.java
  Files are paired by their path relative to each tree root. A file present only in the
  refactored tree (a brand-new class) is skipped: there is no original to diff against.

For each paired class we find the methods whose body changed, keep the AUTO_FUZZABLE ones
(scalar/String/array args, top-level — see auto_select.classify), and:
  - copy the original file  -> src/test/Dataset/<project>/<pkg>/<Class>Original.java   (renamed)
  - copy the refactored file -> src/test/Dataset/<project>/<pkg>/<Class>Refactored.java (renamed)
  - record a manifest entry {id, original FQN, refactored FQN, method, params, static}

Writes src/test/resources/<project>/manifest.json (read at fuzz time by GenericDifferential).
The whole-word rename also fixes self-references (e.g. `VersionInfo x = new VersionInfo()`).
"""
import argparse
import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from auto_select import methods, classify, package  # noqa: E402

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

JAVA_FQN = {"String": "java.lang.String"}  # primitives + primitive arrays pass through


def java_type(t):
    return JAVA_FQN.get(t, t)


def rename(text, cls, suffix):
    """Whole-word rename of the class's own name (handles decl, ctors, self-refs, .class)."""
    return re.sub(r"\b" + re.escape(cls) + r"\b", cls + suffix, text)


def pair_files(orig_root, ref_root):
    """Yield (rel_path, orig_abs, ref_abs) for every .java present in BOTH trees."""
    for dirpath, _, files in os.walk(ref_root):
        for f in files:
            if not f.endswith(".java"):
                continue
            ref_abs = os.path.join(dirpath, f)
            rel = os.path.relpath(ref_abs, ref_root)
            orig_abs = os.path.join(orig_root, rel)
            if os.path.isfile(orig_abs):
                yield rel, orig_abs, ref_abs


def main(project, orig_root, ref_root):
    orig_root = os.path.abspath(orig_root)
    ref_root = os.path.abspath(ref_root)
    for label, root in (("--original", orig_root), ("--refactored", ref_root)):
        if not os.path.isdir(root):
            print(f"ERROR: {label} tree not found: {root}", file=sys.stderr)
            return 1

    java_root = os.path.join(MODULE, "src/test/Dataset", project)  # per-project source root
    entries = []
    seen = {}
    n_classes = 0

    for rel, orig_p, ref_p in sorted(pair_files(orig_root, ref_root)):
        cls = os.path.splitext(os.path.basename(ref_p))[0]  # filename stem == public class name
        orig_txt = open(orig_p, encoding="utf-8", errors="replace").read()
        impr_txt = open(ref_p, encoding="utf-8", errors="replace").read()
        pkg = package(ref_p)
        if not pkg:
            continue
        o = methods(orig_txt)
        n = methods(impr_txt)

        class_entries = []
        for key in n:
            if key not in o or o[key][0] == n[key][0]:
                continue  # unchanged or added-only
            name = key.split("/")[0].split("#")[0]
            if name == cls:
                continue  # constructor, not a method
            ok, is_static, types = classify(n[key][1], n[key][2])
            if not ok:
                continue  # NEEDS_OBJECT
            mid = f"{cls}.{name}"
            if mid in seen:
                seen[mid] += 1
                mid = f"{mid}_{seen[mid]}"
            else:
                seen[mid] = 0
            class_entries.append({
                "id": mid,
                "original": f"{pkg}.{cls}Original",
                "refactored": f"{pkg}.{cls}Refactored",
                "method": name,
                "params": [java_type(t) for t in types],
                "static": is_static,
            })

        if not class_entries:
            continue

        pkg_dir = os.path.join(java_root, pkg.replace(".", "/"))
        os.makedirs(pkg_dir, exist_ok=True)
        open(os.path.join(pkg_dir, f"{cls}Original.java"), "w", encoding="utf-8").write(rename(orig_txt, cls, "Original"))
        open(os.path.join(pkg_dir, f"{cls}Refactored.java"), "w", encoding="utf-8").write(rename(impr_txt, cls, "Refactored"))
        entries.extend(class_entries)
        n_classes += 1

    res_dir = os.path.join(MODULE, "src/test/resources", project)
    os.makedirs(res_dir, exist_ok=True)
    import json
    with open(os.path.join(res_dir, "manifest.json"), "w") as f:
        json.dump({"project": project, "source": {"original": orig_root, "refactored": ref_root},
                   "methods": entries}, f, indent=2)

    print(f"{project}: {n_classes} classes, {len(entries)} auto-fuzzable methods")
    print(f"  sources  -> src/test/Dataset/{project}/<pkg>/<Class>{{Original,Refactored}}.java")
    print(f"  manifest -> src/test/resources/{project}/manifest.json")
    return 0


if __name__ == "__main__":
    ap = argparse.ArgumentParser(description="Build manifest + snapshots from two source trees.")
    ap.add_argument("project")
    ap.add_argument("--original", required=True, help="original source tree root")
    ap.add_argument("--refactored", required=True, help="refactored source tree root")
    args = ap.parse_args()
    sys.exit(main(args.project, args.original, args.refactored))
