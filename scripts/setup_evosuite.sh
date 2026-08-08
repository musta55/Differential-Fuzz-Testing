#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Provision EvoSuite, the automated unit-test generator that produces the seed
# corpus for the fuzzer (and a coverage baseline to compare the fuzzer against).
#
#   Usage: scripts/setup_evosuite.sh [<dir-with-existing-jars>]
#
# With no argument it downloads the official v1.2.0 release jars into tools/evosuite/.
# Given a directory, it copies the jars from there instead (offline / shared machine).
# Either way it also installs evosuite-standalone-runtime into the local Maven repo as
# org.evosuite.local:evosuite-standalone-runtime:1.2.0-local, which is what the generated
# *_ESTest_scaffolding.java needs on the classpath to compile and run.
#
# Version 1.2.0 is NOT on Maven Central (only the older 1.0.6 line is), so the jars come
# from the GitHub release. EvoSuite 1.2.0 also requires a Java 8 JVM to run — it reaches
# into JDK internals that are sealed from Java 9 on. The build itself is unaffected.
#
# Idempotent: existing jars are left alone unless --force is passed.
# ---------------------------------------------------------------------------
set -euo pipefail

VER="1.2.0"
BASE="https://github.com/EvoSuite/evosuite/releases/download/v${VER}"
MODULE="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DEST="$MODULE/tools/evosuite"
GID="org.evosuite.local"
LOCAL_VER="${VER}-local"

FORCE=0
SRC=""
for a in "$@"; do
  case "$a" in
    --force) FORCE=1 ;;
    *) SRC="$a" ;;
  esac
done

mkdir -p "$DEST"

fetch() {
  local name="$1" out="$DEST/$1"
  if [[ -s "$out" && $FORCE -eq 0 ]]; then
    echo "  = $name (already present)"
    return
  fi
  if [[ -n "$SRC" ]]; then
    if [[ ! -s "$SRC/$name" ]]; then
      echo "  ✗ $name not found in $SRC" >&2
      exit 1
    fi
    cp "$SRC/$name" "$out"
    echo "  ✓ $name (copied from $SRC)"
  else
    echo "  … downloading $name"
    curl -fsSL -o "$out" "$BASE/$name"
    echo "  ✓ $name ($(du -h "$out" | cut -f1))"
  fi
}

echo "Provisioning EvoSuite $VER into $DEST"
fetch "evosuite-${VER}.jar"
fetch "evosuite-standalone-runtime-${VER}.jar"

echo
echo "Installing the standalone runtime as ${GID}:evosuite-standalone-runtime:${LOCAL_VER}"
"$MODULE/mvnw" -q install:install-file \
    -Dfile="$DEST/evosuite-standalone-runtime-${VER}.jar" \
    -DgroupId="$GID" -DartifactId="evosuite-standalone-runtime" \
    -Dversion="$LOCAL_VER" -Dpackaging=jar
echo "  ✓ installed"

# EvoSuite 1.2.0 only runs on Java 8. Warn rather than fail: the generated tests compile
# and run on any JDK, so a mismatch only matters at generation time.
if ! java -version 2>&1 | grep -q '"1\.8'; then
  echo
  echo "  ! Default java is not 8 — EvoSuite $VER will likely fail to start."
  echo "    Point JAVA8_HOME at a JDK 8 (scripts/gen_unittests.py honours it), e.g.:"
  echo "      export JAVA8_HOME=/usr/lib/jvm/java-8-openjdk-amd64"
fi

echo
echo "Done. Generate a seed suite with:"
echo "  python3 scripts/gen_unittests.py <project>"
