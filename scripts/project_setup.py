#!/usr/bin/env python3

"""
Builds a differential-fuzz-testing fat jar for a target Java project and
registers it as a Maven profile in this repo's pom.xml.

Given a project directory, this script:
  1. Locates the project's build file (pom.xml, build.gradle(.kts), or
     falls back to plain javac if no build file is found).
  2. Builds a fat jar for the project:
     - Maven: installs the reactor, discovers every jar-packaged module
       (including nested multi-module projects), and shades them all into
       a single fat jar via a synthetic pom.
     - Gradle: invokes a custom init script/task
       (differential-fuzz-testing-fatjar.gradle) to produce the fat jar.
     - No build file: compiles all .java sources under src/main/java (or
       the project root) with javac and jars the resulting classes.
  3. Adds/replaces a <profile> in this repo's pom.xml (keyed by project
     name) that depends on the built jar via a systemPath dependency and
     wires up the project's Dataset/fuzzing test sources.

Usage:
    ./project_setup.py <project-name> -d /path/to/project/dir
"""

import argparse
import subprocess
import xml.etree.ElementTree as XML
from collections import deque
from pathlib import Path

GRADLE_JAR_INIT_SCRIPT_PATH = (Path(__file__).resolve().parent / "differential-fuzz-testing-fatjar.gradle")

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

def compile_jar(build_file: Path | None, project_dir: Path):
    """
    Compiles a fat jar for the current project, and returns the path.
    May raise if the jarring did not work for any reason.
    :return: The path to the fat jar
    """

    if build_file is None:
        candidates = build_javac_jar(project_dir)
    elif build_file.name == "pom.xml":
        candidates = build_maven_jar(build_file)
    else:
        candidates = build_gradle_jar(project_dir)

    if not candidates:
        raise FileNotFoundError(f"No jar found after build in {project_dir}")

    return max(candidates, key=lambda p: p.stat().st_mtime)

def build_javac_jar(project_dir: Path) -> list[Path]:
    """
    Compiles all .java sources under project_dir with javac and packages
    the resulting classes into a jar. Used when no build file (pom.xml/
    build.gradle) is present.
    :return: List of candidate jar paths (single-element, for consistency
             with build_maven_jar/build_gradle_jar).
    """
    src_root = project_dir / "src" / "main" / "java"
    if not src_root.is_dir():
        src_root = project_dir

    project_dir = project_dir.resolve()

    sources = [s.resolve().relative_to(project_dir) for s in src_root.rglob("*.java")]
    if not sources:
        raise FileNotFoundError(f"No .java sources found in {src_root}")

    classes_dir = project_dir / "_javac_build"
    classes_dir.mkdir(exist_ok=True)

    subprocess.run(
        ["javac", "-d", str(classes_dir), *[str(s) for s in sources]],
        check=True,
        cwd=project_dir,
    )

    jar_path = classes_dir / f"{project_dir.name}.jar"
    subprocess.run(
        ["jar", "cf", str(jar_path), "-C", str(classes_dir), "."],
        check=True,
    )

    return [jar_path]

def build_gradle_jar(directory: Path) -> list[Path]:
    """Build the project's differential-fuzz-testing-classified fat jar via an init script."""
    subprocess.run(
        ["./gradlew", "-I", GRADLE_JAR_INIT_SCRIPT_PATH, "differentialFuzzTestingFatJar"],
        cwd=directory,
        check=True,
        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )
    return list(directory.rglob("build/libs/*-differential-fuzz-testing.jar"))


def build_maven_jar(pom_xml_path: Path) -> list[Path]:
    """Build a single differential-fuzz-testing-classified fat jar containing every jar-packaged
    module reachable from pom.xml."""
    project_dir = pom_xml_path.parent

    subprocess.run(
        ["./mvnw", "install", "-Dmaven.test.skip=true"],
        cwd=project_dir,
        check=True,
        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )

    gavs = _discover_jar_modules(pom_xml_path, project_dir)
    if not gavs:
        raise RuntimeError(f"No jar-packaged modules found under {pom_xml_path}")

    synthetic_pom = project_dir / "pom-differential-fuzz-testing.xml"
    _write_fatjar_pom(synthetic_pom, gavs)

    subprocess.run(
        ["./mvnw", "-f", synthetic_pom.name, "package", "-Dmaven.test.skip=true"],
        cwd=project_dir,
        check=True,
        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )
    return list(project_dir.rglob("target/*-differential-fuzz-testing.jar"))


def _find_build_system_file(base_dir: Path) -> Path | None:
    """
    Detects the build system file in a folder. Breadth-first search for
    either build.gradle(.kts) or pom.xml, and returns None if
    neither was found.
    :param base_dir: The base directory
    :return: "maven", "gradle", or None
    """
    queue = deque[Path]([base_dir])
    ignored = {".git", "target", "build", ".gradle"}

    while queue:
        current = queue.popleft()

        for path in current.iterdir():
            if path.is_file() and path.name in ("pom.xml", "build.gradle", "build.gradle.kts"):
                return path
            elif path.is_dir() and path.name not in ignored:
                queue.append(path)

    return None

def _write_fatjar_pom(pom_path: Path, gavs: list[tuple[str, str, str]]) -> None:
    dependencies_xml = "\n".join(
        DEPENDENCY_TEMPLATE.format(group_id=g, artifact_id=a, version=v)
        for g, a, v in gavs
    )
    pom_path.write_text(
        FATJAR_POM_TEMPLATE.format(dependencies=dependencies_xml),
        encoding="utf-8",
    )


def _strip_ns(tag: str) -> str:
    return tag.split("}", 1)[-1] if "}" in tag else tag


def _child_text(elem: XML.Element, name: str) -> str | None:
    for child in elem:
        if _strip_ns(child.tag) == name:
            return child.text
    return None


def _resolve_gav(pom_path: Path, project_dir: Path) -> tuple[str | None, str | None, str | None, str]:
    """Returns (groupId, artifactId, version, packaging) from the effective POM,
    i.e. with parent inheritance, profiles, and property substitution already
    applied by Maven itself."""
    result = subprocess.run(
        ["./mvnw", "help:effective-pom", "-q", "-f", str(pom_path.resolve()), "-Doutput=/dev/stdout"],
        capture_output=True,
        text=True,
        check=True,
        cwd=project_dir
    )

    xml_start = result.stdout.index("<")
    root = XML.fromstring(result.stdout[xml_start:])

    # Aggregator/multi-module POMs print a <projects> wrapper containing
    # one <project> per module; a single-module POM prints <project> directly.
    project_elem = root if _strip_ns(root.tag) == "project" else next(
        c for c in root if _strip_ns(c.tag) == "project"
    )

    group_id = _child_text(project_elem, "groupId")
    artifact_id = _child_text(project_elem, "artifactId")
    version = _child_text(project_elem, "version")
    packaging = _child_text(project_elem, "packaging") or "jar"

    return group_id, artifact_id, version, packaging


def _discover_jar_modules(
        pom_path: Path, project_dir: Path, seen: set[Path] | None = None
) -> list[tuple[str, str, str]]:
    """Recursively walk <module> entries from pom_path, returning
    (groupId, artifactId, version) for every jar-packaged module found,
    at any nesting depth."""
    if seen is None:
        seen = set()

    pom_path = pom_path.resolve()
    if pom_path in seen:
        return []
    seen.add(pom_path)

    root = XML.parse(pom_path).getroot()
    modules_elem = next((c for c in root if _strip_ns(c.tag) == "modules"), None)
    module_names = (
        [m.text for m in modules_elem if _strip_ns(m.tag) == "module" and m.text]
        if modules_elem is not None
        else []
    )

    gavs: list[tuple[str, str, str]] = []

    for name in module_names:
        child_pom = pom_path.parent / name / "pom.xml"
        if child_pom.is_file():
            gavs.extend(_discover_jar_modules(child_pom, project_dir, seen))

    group_id, artifact_id, version, packaging = _resolve_gav(pom_path, project_dir)
    if packaging == "jar" and group_id and artifact_id and version:
        gavs.append((group_id, artifact_id, version))

    return gavs


def _format_maven_profile(jar_path: Path, project_name: str) -> str:
    """
    Format a Maven profile string for the given jar path and project name.
    """
    return f"""<profile xmlns="{POM_NS}">
  <id>{project_name}</id>
  <dependencies>
    <dependency>
      <groupId>{project_name}-differential-fuzz-testing-fatjar</groupId>
      <artifactId>{project_name}-differential-fuzz-testing-fatjar</artifactId>
      <version>1.0.0</version>
      <scope>system</scope>
      <systemPath>{jar_path}</systemPath>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
        <version>3.2.0</version>
        <executions><execution>
          <id>add-{project_name}</id>
          <phase>generate-test-sources</phase>
          <goals><goal>add-test-source</goal></goals>
          <configuration><sources>
            <source>src/test/Dataset/{project_name}</source>
            <source>src/test/fuzzing/{project_name}</source>
          </sources></configuration>
        </execution></executions>
      </plugin>
    </plugins>
  </build>
</profile>
"""


def _add_to_diff_testing_pomxml(jar_path: Path, project: str):
    XML.register_namespace("", POM_NS)
    ns = {"m": POM_NS}

    pom_path = Path(__file__).resolve().parent.parent / "pom.xml"
    tree = XML.parse(pom_path)
    root = tree.getroot()

    profile_str = _format_maven_profile(jar_path.resolve(), project)
    new_profile = XML.fromstring(profile_str)
    new_profile_id = new_profile.find("m:id", ns).text

    profiles = root.find("m:profiles", ns)
    if profiles is None:
        profiles = XML.SubElement(root, f"{{{POM_NS}}}profiles")

    existing = None
    for profile in profiles.findall("m:profile", ns):
        id_el = profile.find("m:id", ns)
        if id_el is not None and id_el.text == new_profile_id:
            existing = profile
            break

    if existing is not None:
        idx = list(profiles).index(existing)
        profiles.remove(existing)
        profiles.insert(idx, new_profile)
    else:
        profiles.append(new_profile)

    tree.write(pom_path, encoding="utf-8", xml_declaration=True)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("project", help="profile id in pom.xml (e.g. apex-core)")
    parser.add_argument("-d", "--directory", type=str,
                        help="Path to the directory containing the target project")

    args = parser.parse_args()

    if not args.directory:
        print("Error: Project directory is required.")
        parser.print_help()
        exit(1)

    project_dir = Path(args.directory)

    build_system_file = _find_build_system_file(project_dir)
    _add_to_diff_testing_pomxml(compile_jar(build_system_file, build_system_file.parent if build_system_file else project_dir), args.project)

if __name__ == "__main__":
    main()