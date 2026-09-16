#!/usr/bin/env python3
"""
Step 0 — make ANY project runnable by the pipeline, with no hand-editing of pom.xml.

    python3 scripts/project_setup.py <name> -d <projectDir> \
            [--original <origTree>] [--refactored <refTree>]

Given a checkout of the target project it:

  1. Builds the project and shades every one of its jar modules — plus their transitive
     third-party dependencies — into one fat jar. Maven (`mvn install` + a synthetic shade
     pom), Gradle (an init script), or plain `javac` when there is no build file.
  2. Works out which JDK feature release that jar was compiled for, by reading the
     class-file major version out of the jar itself. This is the only reliable source:
     a build file can say anything, the bytes cannot.
  3. Splices a `<profile id="<name>">` into this repo's pom.xml that puts the fat jar on
     the classpath, compiles the snapshots at that release, and adds the two generated
     test-source roots.
  4. Records the project in projects.json (see scripts/projects.py), so later steps and
     scripts/run_tmux.sh need only the name.

Everything the old apex-core path did by hand — a helper script installing four bare module
jars into ~/.m2, then ten third-party dependencies typed into the profile because those jars
carried no pom of their own — this does from the project's own build metadata.

The generated profile is delimited by BEGIN/END GENERATED PROFILE comments and is replaced
in place on re-run. The rest of pom.xml, hand-written profiles included, is never touched:
the file is edited as text precisely because an XML round-trip drops every comment in it.

Usage examples:
    # a Maven project, both trees registered in one go
    python3 scripts/project_setup.py deltaspike -d projects/before/deltaspike \
        --original projects/before/deltaspike --refactored projects/after/deltaspike

    # reuse a fat jar built earlier (skips the project build entirely)
    python3 scripts/project_setup.py deltaspike -d projects/before/deltaspike \
        --jar /path/to/deltaspike-differential-fuzz-testing.jar
"""

import argparse
import os
import re
import shlex
import shutil
import subprocess
import sys
import xml.etree.ElementTree as XML
import zipfile
from collections import deque
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import projects  # noqa: E402

MODULE = Path(__file__).resolve().parent.parent
GRADLE_JAR_INIT_SCRIPT_PATH = Path(__file__).resolve().parent / "differential-fuzz-testing-fatjar.gradle"

POM_NS = "http://maven.apache.org/POM/4.0.0"
FATJAR_POM_TEMPLATE = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.differential-fuzz-testing.fatjar</groupId>
    <artifactId>differential-fuzz-testing-fatjar</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <dependencies>
{dependencies}
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.6.2</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <createDependencyReducedPom>false</createDependencyReducedPom>
                            <shadedArtifactAttached>true</shadedArtifactAttached>
                            <shadedClassifierName>differential-fuzz-testing</shadedClassifierName>
                            <!-- A shaded uber-jar inherits every input jar's signature files,
                                 which no longer match the repacked content: the JVM then
                                 refuses to load the classes with SecurityException. -->
                            <filters>
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                        <exclude>module-info.class</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
"""

DEPENDENCY_TEMPLATE = """        <dependency>
            <groupId>{group_id}</groupId>
            <artifactId>{artifact_id}</artifactId>
            <version>{version}</version>
        </dependency>"""

BEGIN = "<!-- BEGIN GENERATED PROFILE: {name} (scripts/project_setup.py) -->"
END = "<!-- END GENERATED PROFILE: {name} -->"
GENERATED_BLOCK = re.compile(
    r"[ \t]*<!-- BEGIN GENERATED PROFILE:.*?<!-- END GENERATED PROFILE:[^>]*-->\n?", re.S)


def handwritten_profile_ids(text: str) -> set[str]:
    """The ids of profiles in pom.xml that this script did not generate.

    Parsed, not pattern-matched. The first version searched for `<profile>\\s*<id>NAME</id>`,
    which misses any profile carrying a comment before its <id> — `example` has one, so
    re-registering that name appended a SECOND <profile><id>example</id> rather than stopping,
    and a pom with two profiles of one id is a model error Maven refuses to build.

    Only writing through ElementTree drops the file's comments; reading it is free.
    """
    try:
        root = XML.fromstring(GENERATED_BLOCK.sub("", text))
    except XML.ParseError:
        return set()          # a pom Maven could not read either — let the splice proceed
    ids = set()
    for ns in (f"{{{POM_NS}}}", ""):
        for profile in root.findall(f"{ns}profiles/{ns}profile"):
            pid = profile.find(f"{ns}id")
            if pid is not None and pid.text:
                ids.add(pid.text.strip())
    return ids


# ── running builds ─────────────────────────────────────────────────────────────────────

def _tool(directory: Path, wrapper: str, fallback: str) -> list[str]:
    """The project's build wrapper if it ships one, else the system tool.

    Not every checkout has a wrapper — deltaspike has no mvnw — and the old code invoked
    './mvnw' unconditionally, so those projects died on FileNotFoundError.
    """
    wrapper_path = directory / wrapper
    if wrapper_path.is_file():
        if not os.access(wrapper_path, os.X_OK):
            wrapper_path.chmod(wrapper_path.stat().st_mode | 0o111)
        return [str(wrapper_path)]
    found = shutil.which(fallback)
    if not found:
        sys.exit(f"neither {directory / wrapper} nor a '{fallback}' on PATH — cannot build {directory}")
    return [found]


def _run(argv: list[str], cwd: Path, what: str, capture: bool = False, tolerate: bool = False):
    """Run a build step, and on failure print its tail instead of swallowing it.

    The previous version sent stdout AND stderr to DEVNULL, so a failed project build
    surfaced as a bare CalledProcessError with nothing to act on.
    """
    print(f"  $ {' '.join(argv)}", flush=True)
    r = subprocess.run(argv, cwd=cwd, text=True,
                       stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    if r.returncode != 0 and not tolerate:
        tail = "\n".join((r.stdout or "").splitlines()[-40:])
        sys.exit(f"\n{what} FAILED (exit {r.returncode}) in {cwd}\n"
                 f"--- last 40 lines ---\n{tail}\n")
    return (r.stdout, r.returncode) if capture else r.returncode


def compile_jar(build_file: Path | None, project_dir: Path, extra_args: list[str]) -> Path:
    """Build the project's fat jar and return its path."""
    if build_file is None:
        candidates = build_javac_jar(project_dir)
    elif build_file.name == "pom.xml":
        candidates = build_maven_jar(build_file, extra_args)
    else:
        candidates = build_gradle_jar(build_file.parent, extra_args)
    if not candidates:
        sys.exit(f"no fat jar found after building {project_dir}")
    return max(candidates, key=lambda p: p.stat().st_mtime)


def build_javac_jar(project_dir: Path) -> list[Path]:
    """No build file: compile everything under src/main/java (or the root) and jar it."""
    src_root = project_dir / "src" / "main" / "java"
    if not src_root.is_dir():
        src_root = project_dir
    project_dir = project_dir.resolve()
    sources = [s.resolve().relative_to(project_dir) for s in src_root.rglob("*.java")]
    if not sources:
        sys.exit(f"no .java sources found under {src_root}")
    classes_dir = project_dir / "_javac_build"
    classes_dir.mkdir(exist_ok=True)
    _run(["javac", "-d", str(classes_dir), *map(str, sources)], project_dir, "javac")
    jar_path = classes_dir / f"{project_dir.name}-differential-fuzz-testing.jar"
    _run(["jar", "cf", str(jar_path), "-C", str(classes_dir), "."], project_dir, "jar")
    return [jar_path]


def build_gradle_jar(directory: Path, extra_args: list[str]) -> list[Path]:
    """Build the fat jar through the bundled init script's differentialFuzzTestingFatJar task."""
    _run([*_tool(directory, "gradlew", "gradle"), "-I", str(GRADLE_JAR_INIT_SCRIPT_PATH),
          "differentialFuzzTestingFatJar", *extra_args], directory, "gradle fat jar")
    return list(directory.rglob("build/libs/*-differential-fuzz-testing.jar"))


def build_maven_jar(pom_xml_path: Path, extra_args: list[str]) -> list[Path]:
    """Install the reactor, then shade every jar module reachable from it into one fat jar.

    A partial install is fine and is common on a large project: skywalking's apm-webapp
    shells out to `npm ci`, which fails on a machine with no npm or no network, and it has
    no Java the fuzzer wants. Only modules that actually produced a jar go into the fat jar,
    so the build runs --fail-at-end and the rest of the reactor is still usable. Aborting
    on the first bad module would make every such project unrunnable for the sake of one
    module the fuzzer has no interest in.
    """
    project_dir = pom_xml_path.parent
    mvn = _tool(project_dir, "mvnw", "mvn")
    # Checks that gate the project's own release build have nothing to say about a
    # classpath jar, and several of them fail on a tree with refactorings applied.
    skips = ["-Dmaven.test.skip=true", "-Drat.skip=true", "-Dcheckstyle.skip=true",
             "-Dmaven.javadoc.skip=true", "-Denforcer.skip=true", "-Dlicense.skip=true",
             "-Dspotless.check.skip=true", "-Danimal.sniffer.skip=true"]
    out, rc = _run([*mvn, "-B", "--fail-at-end", "install", *skips, *extra_args],
                   project_dir, "mvn install", capture=True, tolerate=True)
    if rc != 0:
        bad = re.findall(r"(?m)^\[INFO\] (\S+) \.+ (?:FAILURE|SKIPPED)", out or "")
        if bad:
            print(f"  ! {len(bad)} module(s) did not install: {', '.join(bad)}", file=sys.stderr)
        else:
            # No reactor summary to blame — the failure was outside the per-module build, so
            # show it rather than claiming "0 modules failed" and printing nothing.
            errs = [ln for ln in (out or "").splitlines() if ln.startswith("[ERROR]")][:12]
            print(f"  ! the build exited {rc} with no module marked FAILURE:", file=sys.stderr)
            print("\n".join("    " + e for e in errs) or "    (no [ERROR] lines)", file=sys.stderr)
        print("    continuing with whatever modules produced a jar; use --build-args to change\n"
              "    how the project is built (e.g. --build-args=\'-pl !apm-webapp\')", file=sys.stderr)

    gavs = _discover_jar_modules(pom_xml_path, project_dir, mvn)
    if not gavs:
        sys.exit(f"no jar-packaged modules found under {pom_xml_path}"
                 + ("\n  (every module failed to install — fix the build first)" if rc else ""))
    print(f"  {len(gavs)} jar module(s) to shade")

    synthetic_pom = project_dir / "pom-differential-fuzz-testing.xml"
    synthetic_pom.write_text(
        FATJAR_POM_TEMPLATE.format(dependencies="\n".join(
            DEPENDENCY_TEMPLATE.format(group_id=g, artifact_id=a, version=v) for g, a, v in gavs)),
        encoding="utf-8")
    _run([*mvn, "-B", "-f", synthetic_pom.name, "package", "-Dmaven.test.skip=true"],
         project_dir, "mvn shade")
    return list(project_dir.rglob("target/*-differential-fuzz-testing.jar"))


# ── reading the project's Maven metadata ───────────────────────────────────────────────

def _strip_ns(tag: str) -> str:
    return tag.split("}", 1)[-1] if "}" in tag else tag


def _child(elem: XML.Element, name: str) -> XML.Element | None:
    for child in elem:
        if _strip_ns(child.tag) == name:
            return child
    return None


def _child_text(elem: XML.Element, name: str) -> str | None:
    child = _child(elem, name)
    return child.text if child is not None else None


def _discover_jar_modules(pom_path: Path, project_dir: Path, mvn: list[str]):
    """(groupId, artifactId, version) for every jar module in the reactor.

    ONE `help:effective-pom` for the whole reactor: an aggregator prints a <projects>
    wrapper holding every module's fully-resolved pom, parent inheritance and property
    substitution already applied by Maven. The previous implementation walked <module>
    entries itself and spawned a Maven JVM per module, which on deltaspike's 40 modules
    meant 40 sequential builds just to read four fields each.
    """
    out, _ = _run([*mvn, "-B", "-q", "help:effective-pom", "-f", str(pom_path.resolve()),
                   "-Doutput=/dev/stdout"], project_dir, "mvn help:effective-pom", capture=True)
    start = out.index("<")
    # -q still lets plugin banners through on some Maven versions; keep only the XML document.
    root = XML.fromstring(out[start:])
    elems = [root] if _strip_ns(root.tag) == "project" else \
        [c for c in root if _strip_ns(c.tag) == "project"]
    gavs, seen, missing = [], set(), []
    for e in elems:
        g, a, v = (_child_text(e, "groupId"), _child_text(e, "artifactId"), _child_text(e, "version"))
        if (_child_text(e, "packaging") or "jar") != "jar" or not (g and a and v) or (g, a) in seen:
            continue
        seen.add((g, a))
        # Only modules that actually produced a jar. A module the reactor could not build is
        # not resolvable, and listing it would make the shade step fail on a dependency the
        # fuzzer never needed — which is how one npm-driven module used to sink a whole project.
        build = _child(e, "build")
        outdir = _child_text(build, "directory") if build is not None else None
        if outdir and not (Path(outdir) / f"{a}-{v}.jar").is_file():
            missing.append(a)
            continue
        gavs.append((g, a, v))
    if missing:
        print(f"  skipping {len(missing)} module(s) with no built jar: {', '.join(sorted(missing))}")
    return gavs


def _find_build_system_file(base_dir: Path) -> Path | None:
    """Breadth-first search for pom.xml / build.gradle(.kts); None if the project has neither."""
    queue = deque([base_dir])
    ignored = {".git", "target", "build", ".gradle", "node_modules"}
    while queue:
        for path in sorted(queue.popleft().iterdir()):
            if path.is_file() and path.name in ("pom.xml", "build.gradle", "build.gradle.kts"):
                return path
            if path.is_dir() and path.name not in ignored:
                queue.append(path)
    return None


# ── which JDK the snapshots have to be compiled with ───────────────────────────────────

def jar_java_release(jar_path: Path) -> str | None:
    """The highest JDK feature release any class in the jar was compiled for.

    Read from the class-file major version (52 -> 8, 55 -> 11, 61 -> 17 ...), not from the
    build file: this is what the snapshots actually have to link against, and the maximum
    over the whole classpath is precisely what decides whether the current javac can read it.

    Every entry is checked, not a sample. jmeter's fat jar is the reason: 46,470 classes, of
    which exactly SIX are newer than Java 8 — six Jetty ALPN classes at Java 9. Any sampling
    scheme is a coin flip on those, and a full scan of a 117 MB jar costs under two seconds
    in a step that already spent minutes building the project.
    """
    best = 0
    with zipfile.ZipFile(jar_path) as z:
        for name in z.namelist():
            # META-INF/versions/N/ holds multi-release duplicates for NEWER JVMs; the base
            # entries are what a compiler at the project's own level sees.
            if not name.endswith(".class") or "META-INF/versions/" in name:
                continue
            with z.open(name) as f:
                head = f.read(8)
            if len(head) >= 8 and head[:4] == b"\xca\xfe\xba\xbe":
                best = max(best, int.from_bytes(head[6:8], "big"))
    if best < 45:
        return None
    return str(best - 44)


def current_java_release() -> int:
    out = subprocess.run(["javac", "-version"], capture_output=True, text=True)
    m = re.search(r"(\d+)(?:\.(\d+))?", (out.stdout + out.stderr))
    if not m:
        return 0
    major = int(m.group(1))
    return int(m.group(2) or 0) if major == 1 else major


# ── writing the profile into pom.xml ───────────────────────────────────────────────────

def link_jar(jar_path: Path, name: str) -> str:
    """Symlink the fat jar to a stable path inside the repo, and return it repo-relative.

    The profile then reads ${project.basedir}/tools/fatjars/<name>.jar instead of wherever the
    project happens to be checked out, which is what makes the generated pom.xml the same on
    every machine and therefore safe to commit. A symlink, not a copy: openmeetings' jar is
    305 MB. tools/fatjars/ is git-ignored.
    """
    link_dir = MODULE / "tools" / "fatjars"
    link_dir.mkdir(parents=True, exist_ok=True)
    link = link_dir / f"{name}.jar"
    if link.is_symlink() or link.exists():
        link.unlink()
    link.symlink_to(jar_path)
    return link.relative_to(MODULE).as_posix()


def _profile_xml(jar_rel: str, name: str, java: str) -> str:
    return f"""    <profile>
      <id>{name}</id>
      <!-- GENERATED by scripts/project_setup.py — re-run it to refresh; edits here are lost. -->
      <properties>
        <maven.compiler.source>{java}</maven.compiler.source>
        <maven.compiler.target>{java}</maven.compiler.target>
      </properties>
      <dependencies>
        <dependency>
          <groupId>io.fuzztest.fatjar</groupId>
          <artifactId>{name}-differential-fuzz-testing-fatjar</artifactId>
          <version>1.0.0</version>
          <scope>system</scope>
          <systemPath>${{project.basedir}}/{jar_rel}</systemPath>
        </dependency>
      </dependencies>
      <build><plugins>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>build-helper-maven-plugin</artifactId>
          <version>3.2.0</version>
          <executions><execution>
            <id>add-{name}</id>
            <phase>generate-test-sources</phase>
            <goals><goal>add-test-source</goal></goals>
            <configuration><sources>
              <source>src/test/Dataset/{name}</source>
              <source>src/test/fuzzing/{name}</source>
            </sources></configuration>
          </execution></executions>
        </plugin>
      </plugins></build>
    </profile>"""


def write_profile(jar_rel: str, name: str, java: str) -> None:
    """Splice the profile into pom.xml between its markers, as text.

    Deliberately NOT an XML round-trip: xml.etree drops comments, and one run of the previous
    version stripped all 25 explanatory comments out of this repo's pom.xml (9.6 KB -> 7.1 KB).

    Both edits touch only whitespace that belongs to the markers themselves — the indentation
    in front of BEGIN and the newline after END. An earlier attempt trimmed back to the
    previous line break instead, which silently deleted the </profile> above the block.
    """
    pom = MODULE / "pom.xml"
    text = pom.read_text(encoding="utf-8")
    begin, end = BEGIN.format(name=name), END.format(name=name)
    block = f"    {begin}\n{_profile_xml(jar_rel, name, java)}\n    {end}\n"

    if begin in text and end in text:
        pre, rest = text.split(begin, 1)
        _, post = rest.split(end, 1)
        text = pre.rstrip(" \t") + block + post.lstrip("\n")
        action = "replaced"
    else:
        # A profile with this id but no markers is somebody's hand-written one. Overwriting it
        # would be silent data loss, so stop — but say exactly where it is and how to pick the
        # run back up, because by this point the project has already been built and shaded.
        if name in handwritten_profile_ids(text):
            sys.exit(
                f"pom.xml already has a HAND-WRITTEN profile '{name}' with no generated markers.\n"
                f"  --remove will not help: it only deletes profiles this script generated.\n"
                f"  Delete that <profile>...</profile> block by hand, or register this project\n"
                f"  under a different name.\n"
                f"  The fat jar is already built and linked, so skip the rebuild on the retry:\n"
                f"    python3 scripts/project_setup.py {name} --jar {MODULE / jar_rel}")
        if "</profiles>" in text:
            m = re.search(r"(?m)^([ \t]*)</profiles>", text)
            close = (m.group(1) if m else "  ") + "</profiles>"
            text = text.replace(close, block + close, 1)
        else:
            text = text.replace("</project>", f"  <profiles>\n{block}  </profiles>\n</project>", 1)
        action = "added"
    # Atomic: a half-written pom.xml breaks every Maven invocation in the repo, and a run in
    # another terminal reads this file once per fuzzed method.
    tmp = pom.with_suffix(".xml.tmp")
    tmp.write_text(text, encoding="utf-8")
    os.replace(tmp, pom)
    print(f"  {action} <profile>{name}</profile> in pom.xml (java {java})")


def remove_profile(name: str) -> bool:
    """Delete a generated profile block from pom.xml. Hand-written profiles are left alone.

    The profile itself is machine-independent (see link_jar), so removing it is not a cleanup
    obligation — it is here to retire a project, or to free the name for re-registering under
    different trees. The symlink in tools/fatjars/ and the built jar are both left in place.
    """
    pom = MODULE / "pom.xml"
    text = pom.read_text(encoding="utf-8")
    begin, end = BEGIN.format(name=name), END.format(name=name)
    if begin not in text or end not in text:
        return False
    pre, rest = text.split(begin, 1)
    _, post = rest.split(end, 1)
    text = pre.rstrip(" \t") + post.lstrip("\n")
    tmp = pom.with_suffix(".xml.tmp")
    tmp.write_text(text, encoding="utf-8")
    os.replace(tmp, pom)
    return True


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("project", help="name for this project: the pom profile id and registry key")
    ap.add_argument("-d", "--directory", help="a checkout of the target project, to build for the classpath")
    ap.add_argument("--jar", help="use this already-built fat jar instead of building one")
    ap.add_argument("--original", help="original source tree, recorded in projects.json")
    ap.add_argument("--refactored", help="refactored source tree, recorded in projects.json")
    ap.add_argument("--java", help="override the detected JDK release for the profile (e.g. 11)")
    ap.add_argument("--build-args", metavar="ARGS",
                    help="extra arguments for the project's own build, e.g. --build-args='-pl !apm-webapp'")
    ap.add_argument("--remove", action="store_true",
                    help="unregister: delete this project's generated profile from pom.xml and its "
                         "projects.json entry. The built jar is left on disk.")
    args = ap.parse_args()

    if args.remove:
        gone = remove_profile(args.project)
        dropped = projects.drop(args.project)
        print(f"{args.project}: profile {'removed from' if gone else 'not found in'} pom.xml, "
              f"registry entry {'dropped' if dropped else 'not found'}")
        return

    if not args.directory and not args.jar:
        ap.error("one of -d/--directory or --jar is required")

    if args.jar:
        jar = Path(args.jar).resolve()
        if not jar.is_file():
            sys.exit(f"--jar not found: {jar}")
        print(f"Using existing fat jar: {jar}")
    else:
        project_dir = Path(args.directory).resolve()
        if not project_dir.is_dir():
            sys.exit(f"-d/--directory not found: {project_dir}")
        build_file = _find_build_system_file(project_dir)
        kind = build_file.name if build_file else "javac (no build file)"
        print(f"Building {args.project} from {project_dir}  [{kind}]")
        jar = compile_jar(build_file, build_file.parent if build_file else project_dir,
                          shlex.split(args.build_args or "")).resolve()
        print(f"  fat jar: {jar}  ({jar.stat().st_size / 1e6:.0f} MB)")

    java = args.java or jar_java_release(jar) or "8"
    have = current_java_release()
    if have and int(java) > have:
        print(f"\n  ! this project is Java {java} but `javac` here is {have}.\n"
              f"    Set JAVA_HOME to a JDK {java}+ before running the pipeline, e.g.\n"
              f"      export JAVA_HOME=/usr/lib/jvm/java-{java}-openjdk-amd64\n", file=sys.stderr)

    jar_rel = link_jar(jar, args.project)
    print(f"  linked  {jar_rel} -> {jar}")
    write_profile(jar_rel, args.project, java)

    entry = projects.put(args.project, jar=str(MODULE / jar_rel), jarSource=str(jar), java=java,
                         projectDir=str(Path(args.directory).resolve()) if args.directory else None,
                         original=str(Path(args.original).resolve()) if args.original else None,
                         refactored=str(Path(args.refactored).resolve()) if args.refactored else None)
    print(f"  registered in {os.path.relpath(projects.REGISTRY, MODULE)}")
    if entry.get("original") and entry.get("refactored"):
        print(f"\nReady. Run it with:\n  scripts/run_tmux.sh {args.project} --max 5      # smoke test\n"
              f"  scripts/run_tmux.sh {args.project}               # full run")
    else:
        print(f"\nProfile written. Supply the two trees when you run:\n"
              f"  scripts/run_tmux.sh {args.project} --original <o> --refactored <r>")


if __name__ == "__main__":
    main()
