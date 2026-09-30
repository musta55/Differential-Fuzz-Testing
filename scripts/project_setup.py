#!/usr/bin/env python3
"""
Step 0 — make ANY project runnable by the pipeline, with no hand-editing of pom.xml.

    python3 scripts/project_setup.py <name> --original <origTree> [--refactored <refTree>] \
            [-d <buildDir>]

Given a checkout of the target project — -d if given, else the --original tree — it:

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
    python3 scripts/project_setup.py deltaspike \
        --original projects/before/deltaspike --refactored projects/after/deltaspike

    # reuse a fat jar built earlier (skips the project build entirely)
    python3 scripts/project_setup.py deltaspike --original projects/before/deltaspike \
        --jar /path/to/deltaspike-differential-fuzz-testing.jar
"""

import argparse
import os
import re
import shlex
import shutil
import subprocess
import sys
import time
import xml.etree.ElementTree as XML
import zipfile
from collections import deque
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import projects  # noqa: E402

MODULE = Path(__file__).resolve().parent.parent
GRADLE_JAR_INIT_SCRIPT_PATH = (
    Path(__file__).resolve().parent / "differential-fuzz-testing-fatjar.gradle"
)

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
    r"[ \t]*<!-- BEGIN GENERATED PROFILE:.*?<!-- END GENERATED PROFILE:[^>]*-->\n?",
    re.S,
)


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
        return set()  # a pom Maven could not read either — let the splice proceed
    ids = set()
    for ns in (f"{{{POM_NS}}}", ""):
        for profile in root.findall(f"{ns}profiles/{ns}profile"):
            pid = profile.find(f"{ns}id")
            if pid is not None and pid.text:
                ids.add(pid.text.strip())
    return ids


# ── running builds ─────────────────────────────────────────────────────────────────────


def _tool(directory: Path, wrapper: str, fallback: str) -> list[str]:
    """
    Returns the projects's shipped build wrapper (i.e mvnw script) if it ships one, else the system tool.
    """
    ## checks mvnw wrapper script is present in the project. If present no need to have mvn installed on the system.
    wrapper_path = directory / wrapper
    
    ## makes the script executable to current user --> same to execute "chmod +x mvnw"
    if wrapper_path.is_file():
        if not os.access(wrapper_path, os.X_OK):
            wrapper_path.chmod(wrapper_path.stat().st_mode | 0o111)
        return [str(wrapper_path)]
    
    ## if mvnw is not present, check if mvn is present in the system
    found = shutil.which(fallback)
    
    if not found:
        sys.exit(
            f"Neither {directory / wrapper} nor a '{fallback}' on PATH. Hence cannot build {directory}!"
        )
    
    ## this would retun mvn path in the system. e.g., /usr/bin/mvn in ubuntu 20.04
    return [found]


def _run(argv: list[str], cwd: Path, what: str, capture: bool = True, tolerate: bool = False):
    """
    Run a build step.
    Inputs:
     - argv - list of command line arguments to run the build step e.g.,: /usr/bin/mvn -B --fail-at-end clean install -Dmaven.test.skip=true
     - cwd - path of the original/path/project directory where the build step is to be run
     - what - running instruction e.g.,: mvn clean install
     - capture - flag to capture the execution output. If True, the function will return the output and return code of the build step. If False, it will return only the return code.
     - tolerate - flag to tolerate build failures. If True, the function will not exit on build failures. This handles when multi module project fails some modules but still produces some jars.
    """
    print(f"  $ {' '.join(argv)}", flush=True)
    ## stdout=subprocess.PIPE - This will capture the output of the subprocess and store it in memory instead of printing in the console
    ## stderr=subprocess.STDOUT - This will capture erros and redirect it to stdout and thus all logs and errors will be on the same log
    r = subprocess.run(
        argv, cwd=str(cwd), text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT
    )

    ## DEBUG Purposes! Uncomment next line this to DEBUG Purposes: save the output to the log; this will print the output in the console.
    ## print(r.stdout, end="", flush=True) 
    
    ## returncode == 0 --> successful execution of the build step.
    if r.returncode != 0 and not tolerate:
        tail = "\n".join((r.stdout or "").splitlines()[-40:])
        sys.exit(
            f"\n{what} FAILED (exit {r.returncode}) in {cwd}\n"
            f"--- last 40 lines ---\n{tail}\n"
        )
    
    ## return the returncode and outout of the build step if capture = True
    return (r.stdout, r.returncode) if capture else r.returncode


def compile_jar(build_file: Path, project_dir: Path, extra_args: list[str]) -> Path:
    """
    Build the project's fat jar and return its path.
    """
    ## use Javac
    if build_file is None:
        candidates = build_javac_jar(project_dir)
    ## use maven
    elif build_file.name == "pom.xml":
        candidates = build_maven_jar(build_file, project_dir, extra_args)
    ## use gradle
    else:
        candidates = build_gradle_jar(build_file.parent, extra_args)
    if not candidates:
        sys.exit(f"cannot build fat jar for {project_dir}")
    
    ## take the last modified jar file as the fat jar. (skipping the temporary .jar file created by the synthetic pom.xml file)
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
    _run(
        [
            *_tool(directory, "gradlew", "gradle"),
            "-I",
            str(GRADLE_JAR_INIT_SCRIPT_PATH),
            "differentialFuzzTestingFatJar",
            *extra_args,
        ],
        directory,
        "gradle fat jar",
    )
    return list(directory.rglob("build/libs/*-differential-fuzz-testing.jar"))


def build_maven_jar(pom_xml_path: Path, project_dir: Path, extra_args: list[str]) -> list[Path]:
    """
    Build every sub module that can be built into a .jar file and compile them into one fat jar.
    """
    project_dir = project_dir.resolve()

    ## get the project build tool path. Either /path/to/original/project/mvnw/script or /system/installed/mvn/path
    mvn = _tool(project_dir, "mvnw", "mvn")

    ## build only the source code not tests
    skips = [
        "-Dmaven.test.skip=true",
        #   "-Drat.skip=true", "-Dcheckstyle.skip=true",
        #  "-Dmaven.javadoc.skip=true", "-Denforcer.skip=true", "-Dlicense.skip=true",
        #  "-Dspotless.check.skip=true", "-Danimal.sniffer.skip=true"
    ]
    
    build_started = time.time()
    out, return_code = _run(
        ## -B --> --batch mode runs maven interactively. DO NOT remove this because it will not print maven color codes and having color codes will break later log read steps
        ## --fail-at-end --> building every module that doesn't depend on the failed one and reports all failures at the end.
        [*mvn, "-B", "--fail-at-end", "clean", "install", *skips, *extra_args],
        project_dir,
        "mvn clean install",
        capture=True, ## if capture is True the function will return the maven build console log output + return code else just return code.
        tolerate=False, ## if tolerate is True, the function will not exit on failure but will return the output and return code instead.
    )
    ### Important!!! DID NOT CHECK Error Scenario! (line 289 - 315)
    bad = 0
    if return_code != 0:
        bad = re.findall(r"(?m)^\[INFO\] (\S+) \.+ (?:FAILURE|SKIPPED)", out or "")
        if bad:
            print(
                f"  ! {len(bad)} module(s) did not install: {', '.join(bad)}",
                file=sys.stderr,
            )
        else:
            # No reactor summary to blame — the failure was outside the per-module build, so
            # show it rather than claiming "0 modules failed" and printing nothing.
            errs = [ln for ln in (out or "").splitlines() if ln.startswith("[ERROR]")][
                :12
            ]
            print(
                f"  ! the build exited {return_code} with no module marked FAILURE:",
                file=sys.stderr,
            )
            print(
                "\n".join("    " + e for e in errs) or "    (no [ERROR] lines)",
                file=sys.stderr,
            )
        print(
            "    continuing with whatever modules produced a jar; use --build-args to change\n"
            "    how the project is built (e.g. --build-args='-pl !apm-webapp')",
            file=sys.stderr,
        )

    gavs = _discover_jar_modules(pom_path=pom_xml_path, project_dir=project_dir, mvn=mvn, built_after=build_started)
    if not gavs:
        sys.exit(
            f"no jar-packaged modules found under {pom_xml_path}"
            + (
                "\n  (every module failed to install — fix the build first)"
                if return_code
                else ""
            )
        )
    print(f"  {len(gavs)} jar module(s) to package to a single fat jar")

    ## create a synthetic pom.xml file in the project directory to build the fat jar. 
    ## The synthetic pom.xml file will include all the dependencies of the original project of all modules.
    
    synthetic_pom = project_dir / "pom-differential-fuzz-testing.xml"
    synthetic_pom.write_text(
        FATJAR_POM_TEMPLATE.format(
            dependencies="\n".join(
                DEPENDENCY_TEMPLATE.format(group_id=g, artifact_id=a, version=v)
                for g, a, v in gavs
            )
        ),
        encoding="utf-8",
    )

    ## executes mvn package command to build the fat jar using the synthetic pom.xml file. 
    ## This will first build syntetic project's own jar and then runs "mvn shade".
    ## maven-shade plugin now pack all the modules together into one fat jar with all the dependencies. 
    _run(
        [*mvn, "-B", "-f", synthetic_pom.name, "package", "-Dmaven.test.skip=true"],
        project_dir,
        "mvn package (shade fat jar)",
    )

    ## from the above executed "mvn package" command it first build a temporary jar file for the synthetic project and then runs "mvn shade" to build the fat jar.
    ## hence there are two .jar files in the target/ directory. One is almost empty temporary .jar file and other is the actual fat jar.
    return list(project_dir.rglob("target/*-differential-fuzz-testing.jar"))


# ── reading the project's Maven metadata ───────────────────────────────────────────────


def _strip_ns(tag: str) -> str:
    """
    Removed the namespace url from the tag name. e.g., {http://maven.apache.org/POM/4.0.0}project --> project
    """
    return tag.split("}", 1)[-1] if "}" in tag else tag


def _child(elem: XML.Element, name: str) -> XML.Element | None:
    """
    returns the child element with the given name, or None if not found.
    """
    for child in elem:
        if _strip_ns(child.tag) == name:
            return child
    return None


def _child_text(elem: XML.Element, name: str) -> str | None:
    """
    returns the text of the child element with the given name, or None if not found.
    """
    child = _child(elem, name)
    return child.text if child is not None else None


def _discover_jar_modules(pom_path: Path, project_dir: Path, mvn: list[str], built_after: float):
    """
    We need X = (groupId, artifactId, version) for every jar module in the original project.
    
    How to get X? using an effective-pom. Each sub module in the project pom does not include groupdId or vesion of its own. Effective-pom gives those information we need to build the finla fat jar

    In technical words effective-pom is an aggregator prints a <projects>
    wrapper holding every module's fully-resolved pom, parent inheritance and property
    substitution already applied by Maven.

    - pom_path - path/to/original/project/pom.xml
    - project_dir - path/to/original/project/source/code
    - build_after - build step started time in milliseconds
    """
    ### Has a limitation: Warning are also append to the stdout. Remedy: save the stdout to file and read from there.
    out, _ = _run(
        argv = [
            *mvn,
            "-B",
            "-q", ## quiet mode. Suppresses [INFO] log lines. Why? Need to understand
            "help:effective-pom", ## effective-pom is the super pom which contains parent POMS, settings.xml and buils automatically by maven plugin
            "-f", str(pom_path.resolve()),
            "-Doutput=/dev/stdout", ## write the XML to memory 
        ],
        cwd = project_dir,
        what = "mvn help:effective-pom",
        capture=True,
    )
    start = out.index("<")
    root = XML.fromstring(out[start:])
    
    ## builds a list of the <project> elements (one per each module) in the effective pom
    ## output format 
    ## <Projects>
    ##      <project>
    ##          <groupId>xx</groupId>
    ##          <artifactId>yy</artifactId>
    ##          <version>zz</version>
    ##          ...
    ##      <project> 
    ##      ...    
    ## </Projects>
    elems = (
        [root]
        if _strip_ns(root.tag) == "project"
        else [c for c in root if _strip_ns(c.tag) == "project"]
    )
    gavs, seen, missing = [], set(), []
    for e in elems:
        ## extracts and assign groupId, artifactId, version from each project item 
        g, a, v = (
            _child_text(e, "groupId"),
            _child_text(e, "artifactId"),
            _child_text(e, "version"),
        )

        ## skips the module if it is not a jar module or if it is already seen (duplicate) or if any of the groupId, artifactId, version is missing
        ## if the packaging is maven-archetype, then they will be skipped because those will not include any .class files. No help to fuzzer.
        ## modules packaged as jar usually do not have <packaging> tag even in the effective pom
        if (
            (_child_text(e, "packaging") or "jar") != "jar"
            or not (g and a and v)
            or (g, a) in seen):
            continue

        ## seen contains elements such as ((org.apache.apex, apex), ...)
        seen.add((g, a))

        # The jar is named by <build><finalName>, which is only <artifactId>-<version> by default:
        build = _child(e, "build")
        
        ## outdir is the target directory where the .jar is built for each module. e.g.,: /path/to/original_project/module/target
        outdir = _child_text(build, "directory") if build is not None else None
        
        ## <finalName> --> which is only <artifactId>-<version> by default
        final_name = (
            _child_text(build, "finalName") if build is not None else None
        ) or f"{a}-{v}"
        jar = Path(outdir) / f"{final_name}.jar" if outdir else None

        ## Keep a module only if THIS build wrote its jar. jar.stat().st_mtime is the jar's last-modified
        ## time, compared with built_after (the time.time() taken just before `mvn clean install` started).
        ##
        ## 1. No jar at all -> skipped, reported as "<artifactId>".
        ##    e.g., bufferserver failed to compile: `clean` deleted its old jar in bufferserver/target/
        ##    and nothing rebuilt it.
        ## 2. Jar exists but is older than built_after -> skipped, reported as "<artifactId> (...earlier build)".
        ##    e.g., bufferserver failed, so --fail-at-end SKIPPED engine, which depends on it. A skipped
        ##    module runs no phases, not even `clean`, so yesterday's engine/target/apex-engine.jar is still there.
        ##    The same happens to a module left out with --build-args='-pl !<module>'.
        ## 3. Jar exists and is newer than built_after -> kept (added to gavs).
        ##    e.g., api built fine: `clean` deleted its old jar and `install` wrote a new one.
        ##
        ## jar is None only when the effective POM has no <build><directory>; the module is then kept unchecked.
        if jar and not (jar.is_file() and jar.stat().st_mtime >= built_after):
            missing.append(
                f"{a} (Using a jar from an earlier build)" if jar.is_file() else a
            )
            continue
        gavs.append((g, a, v))
    
    if missing:
        print(
            f"  skipping {len(missing)} module(s) with no built jar: {', '.join(sorted(missing))}"
        )
    return gavs


def _find_build_system_file(base_dir: Path) -> Path | None:
    """
    Breadth-first search for pom.xml / build.gradle / build.gradle.kts; 
    None if the project has neither.
    """
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
    """
    The highest JDK feature release any class in the jar was compiled for.

    Read from the class-file major version (52 -> 8, 55 -> 11, 61 -> 17 ...)
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
              <source>src/test/fuzzing/{name}</source>
            </sources></configuration>
          </execution></executions>
        </plugin>
      </plugins></build>
    </profile>"""


def write_profile(jar_rel: str, name: str, java: str) -> None:
    """
    Splice the profile into pom.xml between its markers, as text.

    It tells Maven three things for the project: 
    1. fat jar's classpath 
    2. compile at the project's Java level 
    3. and include this project's harness and dataset folders as test code.
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
                f"    python3 scripts/project_setup.py {name} --jar {MODULE / jar_rel}"
            )
        if "</profiles>" in text:
            m = re.search(r"(?m)^([ \t]*)</profiles>", text)
            close = (m.group(1) if m else "  ") + "</profiles>"
            text = text.replace(close, block + close, 1)
        else:
            text = text.replace(
                "</project>", f"  <profiles>\n{block}  </profiles>\n</project>", 1
            )
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
    ap = argparse.ArgumentParser(
        description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter
    )
    ap.add_argument("project", help="name for this project")
    ap.add_argument(
        "--original", help="/path/to/original/project"
    )
    ap.add_argument(
        "--refactored", help="/path/to/refactored/project"
    )
    ap.add_argument(
        "--java", help="override the detected JDK release for the profile (e.g. 11)"
    )
    ap.add_argument(
        "--build-args",
        metavar="ARGS",
        help="extra arguments for the project's own build, e.g. --build-args='-pl !apm-webapp'",
    )
    ap.add_argument(
        "--remove",
        action="store_true",
        help="unregister: delete this project's generated profile from pom.xml and its "
        "projects.json entry. The built jar is left on disk.",
    )
    args = ap.parse_args()

    if args.remove:
        gone = remove_profile(args.project)
        dropped = projects.drop(args.project)
        print(
            f"{args.project}: profile {'removed from' if gone else 'not found in'} pom.xml, "
            f"registry entry {'dropped' if dropped else 'not found'}"
        )
        return

    build_dir = args.original
    # if not build_dir:
    #     ap.error("--original is required")

    project_dir = Path(build_dir).resolve()
    if not project_dir.is_dir():
        sys.exit(f"{project_dir} not found or not a directory")
    build_file = _find_build_system_file(project_dir)
    kind = build_file.name if build_file else "No pom.xml|build.gradle|build.gradle.kts found"
    print(f"Building {args.project} from {project_dir}  [{kind}]")
    jar = compile_jar(
        build_file = build_file,
        project_dir = project_dir ,
        extra_args = shlex.split(args.build_args or ""), ## eg: shlex.split('-Dfoo="a b" -q')  --> ['-Dfoo=a b', '-q']
    ).resolve()
    print(f"  fat jar: {jar}  ({jar.stat().st_size / 1e6:.0f} MB)")

    project_javac_version = args.java or jar_java_release(jar) or "8" ## project jar's java version
    system_javac_version = current_java_release() ## system's javac version
    
    if system_javac_version and (int(project_javac_version) > system_javac_version):
        print(
            f"\n  ! this project is Java {project_javac_version} but `javac` here is {system_javac_version}.\n"
            f"    Set JAVA_HOME to a JDK {project_javac_version}+ before running the pipeline, e.g.\n",
            file=sys.stderr,
        )

    ## create symlink to fat jar in MODULE/tools/fatjars/<project>.jar and return the relative path to the symlink
    jar_rel = link_jar(jar, args.project)
    print(f"  linked  {jar_rel} -> {jar}")

    ## writes a maven profile into the fuzzer module's own pom.xml.
    write_profile(jar_rel, args.project, project_javac_version)

    ## writes project meta data in MODULE/projects.json
    entry = projects.put(
        args.project,
        jar=str(MODULE / jar_rel),
        jarSource=str(jar),
        java=project_javac_version,
        original=str(Path(args.original).resolve()) if args.original else None,
        refactored=str(Path(args.refactored).resolve()) if args.refactored else None,
    )
    print(f"  registered in {os.path.relpath(projects.REGISTRY, MODULE)}")
    # if entry.get("original") and entry.get("refactored"):
    #     print(
    #         f"\nReady. Run it with:\n  scripts/run_tmux.sh {args.project} --max 5      # smoke test\n"
    #         f"  scripts/run_tmux.sh {args.project}               # full run"
    #     )
    # else:
    #     print(
    #         f"\nProfile written. Supply the two trees when you run:\n"
    #         f"  scripts/run_tmux.sh {args.project} --original <o> --refactored <r>"
    #     )


if __name__ == "__main__":
    main()
