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
from auto_select import (  # noqa: E402
    methods, classify, package, top_level_type_spans, strip_spans,
)

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

# Manifest param types must be FQNs the engine's classFor() can Class.forName().
# Primitives + primitive arrays pass through (classFor handles those literally);
# String + the boxed wrappers need java.lang.* so Class.forName resolves them.
JAVA_FQN = {
    "String": "java.lang.String",
    "Integer": "java.lang.Integer", "Long": "java.lang.Long",
    "Short": "java.lang.Short", "Byte": "java.lang.Byte",
    "Character": "java.lang.Character", "Boolean": "java.lang.Boolean",
    "Float": "java.lang.Float", "Double": "java.lang.Double",
}


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


def _norm(text):
    return re.sub(r"\s+", " ", text).strip()


def secondary_types(orig_txt, impr_txt, cls):
    """Classify the non-primary top-level types a file declares alongside the public class `cls`.

    rename() only renames the primary class, so a helper like `class Foo {}` would be copied
    verbatim into BOTH <Class>{Original,Refactored}.java -> duplicate class in one package ->
    the pair won't compile and gets pruned (this is the issue's `bar(Foo)` breakage).

    Returns (shared_orig_spans, shared_ref_spans, conflict):
      - shared_*_spans: secondaries present on BOTH sides with identical (whitespace-normalized)
        source -> safe to relocate to a single shared file that both snapshots reference.
      - conflict: True iff some secondary is on both sides but DIFFERS -> can't share without
        per-side renaming; caller skips the file.
    Secondaries on only one side cause no collision, so they're left in place (not returned).
    If the primary class isn't locatable on a side, relocate nothing (fall back to old behavior).
    """
    o_all = top_level_type_spans(orig_txt)
    n_all = top_level_type_spans(impr_txt)
    if cls not in {s[0] for s in o_all} or cls not in {s[0] for s in n_all}:
        return [], [], False
    o_secs = {name: (a, b) for name, a, b in o_all if name != cls}
    n_secs = {name: (a, b) for name, a, b in n_all if name != cls}
    shared_o, shared_n, conflict = [], [], False
    for name in set(o_secs) & set(n_secs):
        oa, ob = o_secs[name]
        na, nb = n_secs[name]
        if _norm(orig_txt[oa:ob]) == _norm(impr_txt[na:nb]):
            shared_o.append((name, oa, ob))
            shared_n.append((name, na, nb))
        else:
            conflict = True
    return shared_o, shared_n, conflict


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
    written_secondaries = {}  # fqn -> normalized content, so a shared helper is emitted once
    n_classes = 0

    for rel, orig_p, ref_p in sorted(pair_files(orig_root, ref_root)):
        cls = os.path.splitext(os.path.basename(ref_p))[0]  # filename stem == public class name
        orig_txt = open(orig_p, encoding="utf-8", errors="replace").read()
        impr_txt = open(ref_p, encoding="utf-8", errors="replace").read()
        pkg = package(ref_p)
        if not pkg:
            continue

        # Fix C: pull shared secondary top-level classes out of the snapshot files so they
        # don't collide. Parse the PRIMARY class in isolation (stripped text) so helper-class
        # methods aren't misattributed to it either.
        o_secs, n_secs, conflict = secondary_types(orig_txt, impr_txt, cls)
        if conflict:
            print(f"  skip {rel}: a secondary top-level class differs between trees "
                  f"(needs per-side handling)", file=sys.stderr)
            continue
        orig_primary = strip_spans(orig_txt, o_secs)
        impr_primary = strip_spans(impr_txt, n_secs)

        o = methods(orig_primary)
        n = methods(impr_primary)

        class_entries = []
        for key in n:
            if key not in o or o[key][0] == n[key][0]:
                continue  # unchanged or added-only
            name = key.split("/")[0].split("#")[0]
            is_ctor = (name == cls)  # A2: constructors are tested as first-class units now
            ok, is_static, types = classify(n[key][1], n[key][2])
            if not ok:
                continue  # NEEDS_OBJECT (a param the engine can't synthesize from fuzz bytes)
            mid = f"{cls}.ctor" if is_ctor else f"{cls}.{name}"
            if mid in seen:
                seen[mid] += 1
                mid = f"{mid}_{seen[mid]}"
            else:
                seen[mid] = 0
            entry = {
                "id": mid,
                "original": f"{pkg}.{cls}Original",
                "refactored": f"{pkg}.{cls}Refactored",
                "method": "<init>" if is_ctor else name,
                "params": [java_type(t) for t in types],
                "static": False if is_ctor else is_static,
            }
            if is_ctor:
                entry["ctor"] = True
            class_entries.append(entry)

        if not class_entries:
            continue

        # Emit each shared secondary once (dedup across files by FQN; a name clash with a
        # DIFFERENT prior definition means we can't safely share -> skip this file).
        imports = "\n".join(re.findall(r"(?m)^[ \t]*import\b.*?;", orig_txt))
        shared, clash = [], False
        for name, start, end in sorted(o_secs, key=lambda s: s[1]):
            fqn = f"{pkg}.{name}"
            content = f"package {pkg};\n\n{imports}\n\n{orig_txt[start:end]}\n"
            norm = _norm(content)
            if fqn in written_secondaries:
                if written_secondaries[fqn] != norm:
                    clash = True
                    break
            else:
                shared.append((name, content, fqn, norm))
        if clash:
            print(f"  skip {rel}: secondary class name collides with a different definition",
                  file=sys.stderr)
            continue

        pkg_dir = os.path.join(java_root, pkg.replace(".", "/"))
        os.makedirs(pkg_dir, exist_ok=True)
        open(os.path.join(pkg_dir, f"{cls}Original.java"), "w", encoding="utf-8").write(rename(orig_primary, cls, "Original"))
        open(os.path.join(pkg_dir, f"{cls}Refactored.java"), "w", encoding="utf-8").write(rename(impr_primary, cls, "Refactored"))
        for name, content, fqn, norm in shared:
            open(os.path.join(pkg_dir, f"{name}.java"), "w", encoding="utf-8").write(content)
            written_secondaries[fqn] = norm
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
