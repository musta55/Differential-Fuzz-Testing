#!/usr/bin/env python3
"""
Build the manifest + compiled-source snapshots for a project from two parallel source trees.

INPUT CONTRACT (decoupled — no RefAgent/results layout assumed):
    build_project.py <project> --original <origTree> --refactored <refTree>

  <origTree> and <refTree> are two source trees with the SAME package structure, e.g.
      original/com/acme/Foo.java     refactored/com/acme/Foo.java
  Files are paired by their path relative to each tree root. A file present only in the
  refactored tree (a brand-new class) is skipped: there is no original to diff against.

For each paired class, scripts/MethodExtractor.java (JavaParser) finds the methods that changed —
comparing comment-free ASTs, so reformatting and comment edits do not count but literal and
signature edits do — and classifies each parameter list as scalar or object. For those we:
  - copy the original file  -> src/test/Dataset/<project>/<pkg>/<Class>Original.java   (renamed)
  - copy the refactored file -> src/test/Dataset/<project>/<pkg>/<Class>Refactored.java (renamed)
  - record a manifest entry {id, original FQN, refactored FQN, method, params, static}

Writes src/test/resources/<project>/manifest.json (read at fuzz time by GenericDifferential).
The whole-word rename also fixes self-references (e.g. `VersionInfo x = new VersionInfo()`).
"""
import argparse
import json
import os
import re
import subprocess
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from auto_select import (  # noqa: E402
    package, top_level_type_spans, strip_spans,
)

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))


def rename(text, cls, suffix):
    """Whole-word rename of the class's own name (handles decl, ctors, self-refs, .class)."""
    return re.sub(r"\b" + re.escape(cls) + r"\b", cls + suffix, text)


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


JAVAPARSER = "com.github.javaparser:javaparser-core:3.28.2"
JP_JAR = os.path.expanduser(
    "~/.m2/repository/com/github/javaparser/javaparser-core/3.28.2/javaparser-core-3.28.2.jar")
GSON_JAR = os.path.expanduser("~/.m2/repository/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar")


def extractor_classpath():
    """Build scripts/MethodExtractor.java on demand, the way run_project.py builds CovReport."""
    if not os.path.isfile(JP_JAR):
        print(f"  fetching {JAVAPARSER}")
        subprocess.run([os.path.join(MODULE, "mvnw"), "-q", "dependency:get",
                        f"-Dartifact={JAVAPARSER}"], cwd=MODULE, capture_output=True)
    if not os.path.isfile(JP_JAR):
        sys.exit(f"could not obtain {JAVAPARSER}; check network access to Maven Central")
    out = os.path.join(MODULE, "target/parsetool")
    os.makedirs(out, exist_ok=True)
    cp = os.pathsep.join([JP_JAR, GSON_JAR])
    src = os.path.join(MODULE, "scripts/MethodExtractor.java")
    cls = os.path.join(out, "MethodExtractor.class")
    if not os.path.isfile(cls) or os.path.getmtime(src) > os.path.getmtime(cls):
        r = subprocess.run(["javac", "-cp", cp, "-d", out, src],
                           capture_output=True, text=True)
        if r.returncode != 0:
            sys.exit("could not compile MethodExtractor:\n" + r.stdout + r.stderr)
    return os.pathsep.join([out, cp])


def extract(orig_root, ref_root):
    """Run the AST extractor and return {relPath: pairInfo}.

    Method discovery, the changed/unchanged decision and parameter classification all happen in
    MethodExtractor now. The regex parser this replaced silently dropped any method preceded by an
    annotation with a string argument: SIG_RE matched `SuppressWarnings` with a `params` group that
    ran on past the closing paren into the real signature, and because finditer does not overlap,
    the method inside that span was never seen again. That cost apex-core three genuinely changed
    methods, including two rewritten `toArray` implementations.
    """
    cp = extractor_classpath()
    out = os.path.join(MODULE, "target", "ast-methods.json")
    os.makedirs(os.path.dirname(out), exist_ok=True)
    r = subprocess.run(["java", "-cp", cp, "MethodExtractor", orig_root, ref_root, out],
                       capture_output=True, text=True)
    if r.returncode != 0 or not os.path.isfile(out):
        sys.exit("MethodExtractor failed:\n" + r.stdout + r.stderr)
    data = json.load(open(out))
    for prob in data.get("problems", [])[:5]:
        print(f"  parse problem: {prob[:160]}", file=sys.stderr)
    return data


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

    ast = extract(orig_root, ref_root)

    for pair in ast["pairs"]:
        rel = pair["relPath"]
        ref_p = os.path.join(ref_root, rel)
        orig_p = os.path.join(orig_root, rel)
        cls = pair["primaryType"]
        pkg = pair["package"]
        orig_txt = open(orig_p, encoding="utf-8", errors="replace").read()
        impr_txt = open(ref_p, encoding="utf-8", errors="replace").read()

        # Pull shared secondary top-level classes out of the snapshot files so the two renamed
        # copies don't collide on them. Still span-based on the raw text: the AST tells us WHICH
        # types are secondary, but the snapshots are written by editing the original source so they
        # keep their formatting, which printing the AST back out would not.
        o_secs, n_secs, conflict = secondary_types(orig_txt, impr_txt, cls)
        if conflict:
            print(f"  skip {rel}: a secondary top-level class differs between trees "
                  f"(needs per-side handling)", file=sys.stderr)
            continue
        # The snapshot bodies: the original source minus the relocated secondary types.
        orig_primary = strip_spans(orig_txt, o_secs)
        impr_primary = strip_spans(impr_txt, n_secs)

        class_entries = []
        for m in pair["methods"]:
            is_ctor = m["ctor"]
            mid = f"{cls}.ctor" if is_ctor else f"{cls}.{m['simpleName']}"
            if mid in seen:
                seen[mid] += 1
                mid = f"{mid}_{seen[mid]}"
            else:
                seen[mid] = 0
            entry = {
                "id": mid,
                "original": f"{pkg}.{cls}Original",
                "refactored": f"{pkg}.{cls}Refactored",
                "method": m["name"],
                # Source spellings, advisory: the engine resolves by name+arity against the
                # compiled class and reads real types by reflection (see GenericDifferential).
                "params": m["params"],
                "arity": m["arity"],
                "static": m["static"],
                # What the engine will need to build the arguments. `object` means the run
                # depends on Jazzer autofuzz; `scalar` is the classic direct-from-bytes path.
                "kind": m["kind"],
                # Why the differ considered this method changed — "body", or a signature change
                # that the previous body-only comparison could not see at all.
                "change": m["change"],
                "source": {
                    "class": cls,
                    "package": pkg,
                    "original": rel,
                    "refactored": rel,
                },
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

    kinds = {}
    for e in entries:
        kinds[e["kind"]] = kinds.get(e["kind"], 0) + 1
    breakdown = ", ".join(f"{v} {k}" for k, v in sorted(kinds.items()))
    print(f"{project}: {n_classes} classes, {len(entries)} changed methods ({breakdown})")
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
