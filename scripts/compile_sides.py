#!/usr/bin/env python3
"""
Compile both sides of a project under their REAL class names: the two compile gates.

    compile_sides.py <project>

Gate 1, original side: the original version of every changed .java file, compiled against the
    project's fat jar. The jar was built from these very files, so a failure here is an
    ENVIRONMENT problem (classpath, generated code, JDK), not a finding about the refactoring.

Gate 2, refactored side: every changed or new .java file of the refactored tree, compiled TOGETHER
    against the same fat jar. A source file handed to javac replaces the jar's class of the same
    name, so a refactoring that spans several files (a new signature plus its updated callers, a
    new helper class) compiles exactly as it would inside the refactored project. A failure here
    means the refactoring itself does not compile: REFACTORING_BROKEN.

Nothing is renamed. The classes are written to

    target/sides/<project>/original/      and      target/sides/<project>/refactored/

and the fuzzer loads each side in its own classloader (fuzz.auto.SideLoader), so both versions keep
their real names and the rest of the project sees the version it is running with.

Outputs:
    reports/<project>/compile_status.csv   one row per changed file: which gate failed, and why
    manifest.json                          only the methods whose class passed both gates, plus a
                                           "sides" block that tells the engine where to load from
"""
import csv
import glob
import json
import os
import re
import shutil
import subprocess
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import projects  # noqa: E402

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

# javac cannot write any class file while one of its inputs has an error, so failing files are
# removed and the rest compiled again. More than one round is needed when a file only fails once a
# file it depends on has been removed.
MAX_ROUNDS = 10

# Build files whose changes the original fat jar cannot reflect (new dependencies, versions).
BUILD_FILES = {"pom.xml", "build.gradle", "build.gradle.kts", "settings.gradle"}

# "/abs/path/Foo.java:12: error: cannot find symbol"
JAVAC_ERROR = re.compile(r"^(/.+?\.java):(\d+): error: (.+)$", re.M)

# Lombok's annotation processor, the only one javac may run here (see lombok_jar).
LOMBOK_PROCESSOR = "lombok.launch.AnnotationProcessorHider$AnnotationProcessor"


def lombok_jar(root, rels):
    """The local Lombok jar when one of these files uses Lombok, else None.

    Lombok writes code (getters, the `log` field, builders) while javac runs, so a file that uses
    it only compiles with Lombok's processor switched on. Lombok is compile-only, so it is never
    in the fat jar; Maven's local repository has it once the project has been built.
    """
    uses_lombok = False
    for rel in rels:
        with open(os.path.join(root, rel), encoding="utf-8", errors="replace") as f:
            if "import lombok" in f.read():
                uses_lombok = True
                break
    if not uses_lombok:
        return None
    jars = sorted(glob.glob(os.path.expanduser(
        "~/.m2/repository/org/projectlombok/lombok/*/lombok-*.jar")))
    return jars[-1] if jars else None


def is_test_source(rel):
    """Test code needs the project's test dependencies and is never what a refactoring targets."""
    return "/src/test/" in "/" + rel


def walk_tree(root):
    """Yield every file under root as a '/'-separated path relative to it."""
    for dirpath, dirnames, filenames in os.walk(root):
        # target/ is build output (e.g. skywalking's target/delombok copies of every source), never source
        dirnames[:] = sorted(d for d in dirnames if d not in (".git", "target"))
        for name in sorted(filenames):
            yield os.path.relpath(os.path.join(dirpath, name), root).replace(os.sep, "/")


def same_content(a, b):
    with open(a, "rb") as fa, open(b, "rb") as fb:
        return fa.read() == fb.read()


def changed_files(orig_root, ref_root):
    """(changed .java files, changed build files) of the refactored tree, relative paths.

    A .java file counts when it is new or its bytes differ from the original. Unchanged files are
    left out on purpose: the fat jar already holds them, identical on both sides.
    """
    java_files, build_files = [], []
    for rel in walk_tree(ref_root): ## walks all the files except .git
        orig = os.path.join(orig_root, rel)
        ref = os.path.join(ref_root, rel)
        name = rel.rsplit("/", 1)[-1]
        if rel.endswith(".java") and not is_test_source(rel):
            ## add the file to java_files if it's new (no original exists) or different (the bytes don't match).
            if not os.path.isfile(orig) or not same_content(orig, ref):
                java_files.append(rel)
        ## add the file to build_files only if it exists in both projects and its content changed and it is a BUILD_FILE        
        elif name in BUILD_FILES and os.path.isfile(orig) and not same_content(orig, ref):
            build_files.append(rel)
    return java_files, build_files


def run_javac(root, rels, out_dir, fatjar_classpath, java):
    """Compile rels (relative to root) into out_dir. Returns (succeeded, javac output)."""
    os.makedirs(out_dir, exist_ok=True)
    cmd = ["javac", "-d", out_dir, "-encoding", "UTF-8",
           "-g",                          # debug info: JaCoCo needs it for line coverage
           "-nowarn", "-Xlint:-options",  # warnings are never a reason to drop a file
           "-Xmaxerrs", "100000",         # javac stops at 100 errors by default
           "-source", java, "-target", java]
    lombok = lombok_jar(root, rels)
    if lombok:
        # Run Lombok's processor and no other: the ones in the fat jar must still not run.
        fatjar_classpath = fatjar_classpath + [lombok]
        cmd += ["-processorpath", lombok, "-processor", LOMBOK_PROCESSOR]
    else:
        cmd += ["-proc:none"]             # annotation processors in the fat jar must not run
    if fatjar_classpath:
        # Everything a changed file refers to comes from the jar. An empty source path stops javac
        # from compiling a stray .java it finds on the class path instead. (i.e., all the .class files should come from fat jar)
        empty = out_dir + "-no-sources"
        os.makedirs(empty, exist_ok=True)
        cmd += ["-cp", os.pathsep.join(fatjar_classpath), "-sourcepath", empty, "-implicit:none"]
    else:
        # No jar (the bundled example in the fuzzer module): the side's own tree supplies the unchanged classes, and
        # javac compiles the ones the changed files use into the same output directory.
        cmd += ["-sourcepath", root, "-implicit:class"]
    # The file list goes through an argument file: a large refactoring can exceed the OS limit.
    arg_file = out_dir + "-files.txt"
    with open(arg_file, "w", encoding="utf-8") as f:
        for rel in rels:
            f.write('"' + os.path.join(root, rel) + '"\n')
    # javac -d out_dir -encoding UTF-8 -g ... -cp fat.jar -sourcepath empty -implicit:none @arg-file.txt
    # javac will expand this to --> javac -d out_dir -encoding UTF-8 -g ... -cp fat.jar -sourcepath empty -implicit:none Foo1.java Foo2.java ...
    # all the files are compiled as a batch.
    r = subprocess.run(cmd + ["@" + arg_file], capture_output=True, text=True)
    return r.returncode == 0, r.stdout + r.stderr


def compile_side(side, root, rels, out_dir, classpath, java):
    """
    Compile one side, dropping files that do not compile. Returns {rel: first error}.
    
    compile_side compiles one side's changed Java files together against the fat jar, writes the .class files to that side's output folder, 
    and returns which files failed with their first error. 
    """
    failed = {}
    remaining = list(rels)
    for _ in range(MAX_ROUNDS):
        shutil.rmtree(out_dir, ignore_errors=True)
        os.makedirs(out_dir)
        if not remaining:
            return failed
        ok, output = run_javac(root, remaining, out_dir, classpath, java)
        if ok:
            return failed
        first_error = {}
        for m in JAVAC_ERROR.finditer(output):
            first_error.setdefault(os.path.realpath(m.group(1)), f"line {m.group(2)}: {m.group(3)}")
        newly_failed = {}
        for rel in remaining:
            error = first_error.get(os.path.realpath(os.path.join(root, rel)))
            if error:
                newly_failed[rel] = error
        if not newly_failed:
            # An error in no changed file: an unchanged source javac pulled in, or a bad option.
            sys.exit(f"javac failed on the {side} side without an error in a changed file:\n"
                     + output[-3000:])
        failed.update(newly_failed)
        remaining = [rel for rel in remaining if rel not in newly_failed]
    sys.exit(f"the {side} side still does not compile after {MAX_ROUNDS} rounds")


def require_profile(project):
    """
    The <project> profile MUST exist in pom.xml, which created in the project_setup.py.

    An unknown <project> profile is only a warning: `mvn test` then compiles no harnesses, still exits 0, and every
    method comes out as a `harness error` SKIP with nothing saying why.
    """
    pom = open(os.path.join(MODULE, "pom.xml"), encoding="utf-8").read()
    if not re.search(rf"<id>\s*{re.escape(project)}\s*</id>", pom):
        sys.exit(f"pom.xml has no <profile><id>{project}</id>. Register the project first:\n"
                 f"    python3 scripts/project_setup.py {project} --original <origTree>")


def write_status_csv(path, rows):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["File", "Original", "Refactored", "Status", "Error", "Methods"])
        w.writerows(rows)


def main(project):
    require_profile(project)
    manifest_path = os.path.join(MODULE, "src/test/resources", project, "manifest.json")
    if not os.path.isfile(manifest_path):
        sys.exit(f"no manifest for '{project}' — run scripts/build_project_manifest.py first")
    manifest = json.load(open(manifest_path))
    orig_root = manifest["source"]["original"]
    ref_root = manifest["source"]["refactored"]

    jar = (projects.get(project) or {}).get("jar")
    fatjar_classpath = [jar] if jar and os.path.isfile(jar) else []
    # At least 8: a refactoring may use lambdas even in a project whose jar is Java 7, and the
    # classes must still load on the Java 8 JVM that EvoSuite needs.
    java = str(max(8, int(projects.java_release(project) or 8)))

    java_files, build_files = changed_files(orig_root, ref_root)
    for rel in build_files:
        print(f"  ! {rel} differs between the trees; the original fat jar does not reflect "
              f"build changes (e.g. new dependencies)")
    orig_files = [rel for rel in java_files if os.path.isfile(os.path.join(orig_root, rel))]

    sides_dir = os.path.join(MODULE, "target/sides", project)
    orig_out = os.path.join(sides_dir, "original")
    ref_out = os.path.join(sides_dir, "refactored")
    print(f"{project}: {len(java_files)} changed .java files "
          f"({len(java_files) - len(orig_files)} new), java {java}, "
          f"classpath: {fatjar_classpath[0] if fatjar_classpath else 'the source trees (no fat jar)'}")
    compiled_failed_original_classes = compile_side("original", orig_root, orig_files, orig_out, fatjar_classpath, java)
    compiled_failed_refactored_classes = compile_side("refactored", ref_root, java_files, ref_out, fatjar_classpath, java)

    # One status per changed file, and a method is fuzzed only when its file passed both gates.
    methods_by_file = {}
    for e in manifest["methods"]:
        methods_by_file.setdefault(e["source"]["original"], []).append(e["id"])
    status_of, rows = {}, []
    for rel in java_files:
        if rel in compiled_failed_original_classes: ## original class does not compile against fat jar
            status, error = "ENVIRONMENT", compiled_failed_original_classes[rel]
        elif rel in compiled_failed_refactored_classes: ## refactored class does not compile against fat jar
            status, error = "REFACTORING_BROKEN", compiled_failed_refactored_classes[rel]
        else:
            status, error = "OK", ""
        status_of[rel] = status
        rows.append([rel,
                     "FAIL" if rel in compiled_failed_original_classes else ("ok" if rel in orig_files else "new file"),
                     "FAIL" if rel in compiled_failed_refactored_classes else "ok",
                     status, error, ";".join(methods_by_file.get(rel, []))])
    status_csv = os.path.join(MODULE, "reports", project, "compile_status.csv")
    write_status_csv(status_csv, rows)

    before = len(manifest["methods"])
    manifest["methods"] = [e for e in manifest["methods"]
                           if status_of.get(e["source"]["original"]) == "OK"]
    manifest["sides"] = {"original": orig_out, "refactored": ref_out, "classpath": fatjar_classpath}
    with open(manifest_path, "w") as f:
        json.dump(manifest, f, indent=2)

    print(f"  gate 1 (original):   {len(orig_files) - len(compiled_failed_original_classes)}/{len(orig_files)} files compile")
    print(f"  gate 2 (refactored): {len(java_files) - len(compiled_failed_refactored_classes)}/{len(java_files)} files compile")
    for rel in java_files:
        if status_of[rel] != "OK":
            print(f"    {status_of[rel]:<18} {rel}: {compiled_failed_original_classes.get(rel) or compiled_failed_refactored_classes.get(rel)}")
    print(f"  methods to fuzz: {len(manifest['methods'])}/{before}   "
          f"(details: {os.path.relpath(status_csv, MODULE)})")
    return 0


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print(__doc__)
        sys.exit(2)
    sys.exit(main(sys.argv[1]))
