#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Install a target project's ALREADY-BUILT classes as local Maven jars, so the
# <Class>Original / <Class>Refactored snapshots resolve the rest of the project.
#
# This is the apex-core provisioning step (the -Papex-core profile depends on
# org.apache.apex.local:apex-*:3.7.0-local, which this produces). It is decoupled
# from any parent repo: point it at wherever you built apex-core.
#
#   Usage: scripts/setup_deps.sh <path-to-built-apex-core>
#          # <path> contains <module>/target/classes for: api common bufferserver engine
#          # e.g. a checkout built with `mvn install -DskipTests`
#
# Alternative: skip this and switch the four apex deps in the apex-core profile to
# Maven Central `org.apache.apex:apex-*:3.7.0`.
#
# Idempotent: re-running just re-installs. Safe.
# ---------------------------------------------------------------------------
set -euo pipefail

BUILT="${1:-${APEX_CORE_HOME:-}}"
GID="org.apache.apex.local"
VER="3.7.0-local"
MODULES=(api common bufferserver engine)

if [[ -z "$BUILT" ]]; then
  echo "usage: scripts/setup_deps.sh <path-to-built-apex-core>   (or set APEX_CORE_HOME)" >&2
  exit 2
fi
BUILT="$(cd "$BUILT" && pwd)"

echo "Installing built apex-core modules as local Maven jars (${GID}:*:${VER})"
echo "  source tree: $BUILT"
echo

for m in "${MODULES[@]}"; do
  classes="$BUILT/$m/target/classes"
  if [[ ! -d "$classes" ]]; then
    echo "  ✗ $m: $classes missing — build apex-core first (mvn install -DskipTests)" >&2
    exit 1
  fi
  n=$(find "$classes" -name '*.class' | wc -l | tr -d ' ')
  jar="$(mktemp -d)/apex-local-$m-$VER.jar"
  ( cd "$classes" && jar cf "$jar" . )
  mvn -q install:install-file \
      -Dfile="$jar" \
      -DgroupId="$GID" -DartifactId="apex-$m" -Dversion="$VER" -Dpackaging=jar
  printf "  ✓ apex-%-12s %5s classes  -> %s:apex-%s:%s\n" "$m" "$n" "$GID" "$m" "$VER"
done

echo
echo "Done. Now run:  python3 run.py apex-core --original examples/apex-core/original \\"
echo "                       --refactored examples/apex-core/refactored"
echo "If a harness hits NoClassDefFoundError at RUNTIME, add that third-party lib to the"
echo "apex-core profile in pom.xml (engine jars carry classes, not their transitive deps)."
