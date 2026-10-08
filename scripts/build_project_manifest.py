#!/usr/bin/env python3
"""
Build the manifest of changed methods for a project from two parallel source trees.

INPUT CONTRACT (decoupled — no RefAgent/results layout assumed):
    build_project.py <project> --original <origTree> --refactored <refTree>

  <origTree> and <refTree> are two source trees with the SAME package structure, e.g.
      original/com/acme/Foo.java     refactored/com/acme/Foo.java
  Files are paired by their path relative to each tree root. A file present only in the
  refactored tree (a brand-new class) has no methods to diff, but scripts/compile_sides.py still
  compiles it, because the changed classes may use it.

scripts/MethodExtractor.java (JavaParser) finds the methods that changed — comparing comment-free
ASTs, so reformatting and comment edits do not count but literal and signature edits do — and
classifies each parameter list as scalar or object. Each becomes one manifest entry.

Nothing is copied or renamed here. Both sides keep their real class names; scripts/compile_sides.py
compiles each side into its own directory and the fuzzer loads each in its own classloader.

Writes src/test/resources/<project>/manifest.json (read at fuzz time by GenericDifferential).
"""
import argparse
import json
import os
import subprocess
import sys

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))


JAVAPARSER = "com.github.javaparser:javaparser-core:3.28.2"
JP_JAR = os.path.expanduser(
    "~/.m2/repository/com/github/javaparser/javaparser-core/3.28.2/javaparser-core-3.28.2.jar")
GSON_JAR = os.path.expanduser("~/.m2/repository/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar")


def extractor_classpath():
    """Build scripts/MethodExtractor.java on demand"""
    if not os.path.isfile(JP_JAR):
        print(f"  fetching {JAVAPARSER}")
        result = subprocess.run([os.path.join(MODULE, "mvnw"), "-q", "dependency:get",
                        f"-Dartifact={JAVAPARSER}"], cwd=MODULE, capture_output=True)
        # print(result.stdout, end="", flush=True)
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
        # print(r.stdout, end="", flush=True)
        if r.returncode != 0:
            sys.exit("could not compile MethodExtractor:\n" + r.stdout + r.stderr)
    return os.pathsep.join([out, cp])


def extract_changed_classes(project, orig_root, ref_root):
    """
    Run the AST extractor and return {relPath: pairInfo}.

    Method discovery, the changed/unchanged decision and parameter classification all happen in
    MethodExtractor.java. 
    
    """
    cp = extractor_classpath()
    out = os.path.join(MODULE, "target", "ast-methods.json")
    os.makedirs(os.path.dirname(out), exist_ok=True)
    r = subprocess.run(["java", "-cp", cp, "MethodExtractor", orig_root, ref_root, out, project],
                       capture_output=True, text=True)

    # r = subprocess.run(
    # ["java",
    #  "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005",
    #  "-cp", cp, "MethodExtractor", orig_root, ref_root, out, project],
    # capture_output=True, text=True)

    # print(r.stdout, end="", flush=True)
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

    entries = []
    seen = {}
    n_classes = 0
    ## pair is a class where both original and refactored projects have and have at least one changed method.
    for pair in extract_changed_classes(project, orig_root, ref_root)["pairs"]:
        relative_path = pair["relPath"]
        if "/src/test/" in "/" + relative_path:
            continue  # test code is not what a refactoring targets, and compile_sides skips it
        if "/target/" in "/" + relative_path:
            continue  # build output, not source (e.g. skywalking's target/delombok copies)
        className = pair["primaryType"]
        pkg = pair["package"]
        for method in pair["methods"]:
            is_constructor = method["ctor"]
            methodId = f"{className}.ctor" if is_constructor else f"{className}.{method['simpleName']}"
            if methodId in seen:
                seen[methodId] += 1
                methodId = f"{methodId}_{seen[methodId]}"
            else:
                seen[methodId] = 0
            entry = {
                "id": methodId,
                # Both sides are the same class name, each loaded from its own side directory.
                # Two keys are kept so every tool can address "its" side the same way.
                "original": f"{pkg}.{className}",
                "refactored": f"{pkg}.{className}",
                "method": method["name"],
                # Source spellings, advisory: the engine resolves by name+arity (arity means # of parameters in the signature) against the
                # compiled class and reads real types by reflection (see GenericDifferential).
                "params": method["params"],
                "arity": method["arity"],
                "static": method["static"],
                # What the fuzzing engine will need to build the arguments. `object` means the run
                # depends on Jazzer autofuzz; `scalar` is the classic direct-from-bytes path. - scalar examples - int, long, double []... (see MethodExtractor's SCALAR values)
                "kind": method["kind"],
                # Why the differ considered this method changed — "body", or a signature change
                "change": method["change"],
                "source": {
                    "class": className,
                    "package": pkg,
                    "original": relative_path,
                    "refactored": relative_path,
                },
            }
            if is_constructor:
                entry["ctor"] = True
            entries.append(entry)
        if pair["methods"]:
            n_classes += 1

    manifest_results_dir = os.path.join(MODULE, "src/test/resources", project)
    os.makedirs(manifest_results_dir, exist_ok=True)
    with open(os.path.join(manifest_results_dir, "manifest.json"), "w") as f:
        json.dump({"project": project, "source": {"original": orig_root, "refactored": ref_root},
                   "methods": entries}, f, indent=2)

    kinds = {}
    for e in entries:
        kinds[e["kind"]] = kinds.get(e["kind"], 0) + 1
    breakdown = ", ".join(f"{v} {k}" for k, v in sorted(kinds.items()))
    print(f"{project}: {n_classes} classes, {len(entries)} changed methods ({breakdown})")
    print(f"  manifest -> src/test/resources/{project}/manifest.json")
    return 0


if __name__ == "__main__":
    ap = argparse.ArgumentParser(description="Build the manifest of changed methods.")
    ap.add_argument("project")
    ap.add_argument("--original", required=True, help="original source tree root")
    ap.add_argument("--refactored", required=True, help="refactored source tree root")
    args = ap.parse_args()
    sys.exit(main(args.project, args.original, args.refactored))
