#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Run the whole differential-fuzzing pipeline from scratch, detached, in tmux.
#
#   scripts/run_tmux.sh <project> [options]
#   scripts/run_tmux.sh --list
#
#   --original <dir>     original source tree      ) omit both and they are read from
#   --refactored <dir>   refactored source tree    ) projects.json (scripts/project_setup.py)
#   --duration <t>       Jazzer budget per method          (default 30s)
#   --unit-budget <s>    EvoSuite search seconds per class (default 40)
#   --jobs <n>           EvoSuite classes in parallel      (default 10)
#   --max <n>            smoke test: first N methods, and only their classes
#                        (the bound reaches EvoSuite too, so a 5-method run is minutes)
#   --setup <dir>        build+register the project from this checkout first, then run
#   --build-args <args>  with --setup: extra args for the project's own build
#   --source-seeds       seed from the snapshot sources, not EvoSuite. Needed on any project
#                        built past Java 8: EvoSuite 1.2.0 needs a Java 8 JVM, which cannot
#                        load those snapshots, so step 4 skips and the fuzzer starts unseeded
#   --fg                 run here instead of detaching into tmux
#   --list               show every registered project and exit
#
# NOTHING ABOUT A TARGET PROJECT IS HARD-CODED HERE. This script used to carry a
#   case "$PROJECT" in apex-core) ORIG=...; REF=... ;; example) ... ;; esac
# block, so any project that was not one of those two exited 2, and an apex-only
# prerequisite check ran for everyone. Projects now come from the registry, and the
# prerequisites are derived from the registered project's own entry.
#
# To run a project the first time:
#   python3 scripts/project_setup.py <name> -d <a checkout of the project> \
#       --original <origTree> --refactored <refTree>
#   scripts/run_tmux.sh <name> --max 5        # smoke test the wiring
#   scripts/run_tmux.sh <name>                # full run
# or in one go:  scripts/run_tmux.sh <name> --setup <checkout> --original <o> --refactored <r>
#
# FROM SCRATCH means the generated state is deleted first: the JavaParser tool and its AST
# output, the EvoSuite suites, the encoded seed corpus, the snapshots and harnesses, the
# carried-over libFuzzer corpus, and the previous report. Otherwise a run can pass on stale
# artifacts — EvoSuite reports "reused", the extractor never recompiles, and libFuzzer starts
# from inputs an earlier run discovered, so the report describes history rather than this run.
#
# Budget roughly
#   EvoSuite  ~= (classes / jobs) * (unit-budget + 20s)
#   fuzzing   ~= methods * (duration + ~20s maven overhead)
# apex-core (27 classes / 86 methods) is about 75-90 minutes at the defaults; deltaspike
# (120 classes / 167 methods) is several hours. Use --max 5 to smoke-test the wiring first.
# ---------------------------------------------------------------------------
set -o pipefail

MODULE="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$MODULE"

DURATION="30s"
UNIT_BUDGET="40"
JOBS="10"
ORIG=""
REF=""
SETUP_DIR=""
BUILD_ARGS=""
SRCSEEDS=""
MAXARG=""
FG=0
PROJECT=""
POSITIONAL=()

while [[ $# -gt 0 ]]; do
  case "$1" in
    --list)        python3 scripts/projects.py; exit 0 ;;
    --original)    ORIG="$2";        shift 2 ;;
    --refactored)  REF="$2";         shift 2 ;;
    --duration)    DURATION="$2";    shift 2 ;;
    --unit-budget) UNIT_BUDGET="$2"; shift 2 ;;
    --jobs)        JOBS="$2";        shift 2 ;;
    --setup)       SETUP_DIR="$2";   shift 2 ;;
    --build-args)  BUILD_ARGS="$2";  shift 2 ;;
    --source-seeds) SRCSEEDS="--source-seeds"; shift ;;
    --max)         MAXARG="--max $2"; shift 2 ;;
    --fg)          FG=1;             shift ;;
    -h|--help)     awk 'NR>1 && /^#/ {sub(/^# ?/, ""); print; next} NR>1 {exit}' \
                     "${BASH_SOURCE[0]}"; exit 0 ;;
    -*)            echo "unknown option '$1' (try --help)" >&2; exit 2 ;;
    # Positional back-compat with the old signature:
    #   run_tmux.sh <project> [duration] [unit-budget] [jobs]
    *)             POSITIONAL+=("$1"); shift ;;
  esac
done

PROJECT="${POSITIONAL[0]:-}"
[[ -n "${POSITIONAL[1]:-}" ]] && DURATION="${POSITIONAL[1]}"
[[ -n "${POSITIONAL[2]:-}" ]] && UNIT_BUDGET="${POSITIONAL[2]}"
[[ -n "${POSITIONAL[3]:-}" ]] && JOBS="${POSITIONAL[3]}"

if [[ -z "$PROJECT" ]]; then
  echo "usage: scripts/run_tmux.sh <project> [options]   (--help for all, --list for known projects)" >&2
  echo >&2
  python3 scripts/projects.py >&2
  exit 2
fi

fail() { echo "PREREQ FAILED: $*" >&2; exit 1; }

# ── optional step 0: build + register the project ──────────────────────────
# Done before the tmux relaunch so a setup failure is reported to the terminal the user is
# looking at, rather than into a detached session's log.
if [[ -n "$SETUP_DIR" ]]; then
  echo "---- registering $PROJECT from $SETUP_DIR ----"
  setup_args=(python3 scripts/project_setup.py "$PROJECT" -d "$SETUP_DIR")
  [[ -n "$ORIG" ]] && setup_args+=(--original "$ORIG")
  [[ -n "$REF"  ]] && setup_args+=(--refactored "$REF")
  [[ -n "$BUILD_ARGS" ]] && setup_args+=(--build-args "$BUILD_ARGS")
  "${setup_args[@]}" || fail "project_setup.py failed"
fi

# ── resolve the source trees: explicit flags win, else the registry ────────
[[ -z "$ORIG" ]] && ORIG="$(python3 scripts/projects.py "$PROJECT" original)"
[[ -z "$REF"  ]] && REF="$(python3 scripts/projects.py "$PROJECT" refactored)"
if [[ -z "$ORIG" || -z "$REF" ]]; then
  echo "PREREQ FAILED: no source trees for '$PROJECT'." >&2
  echo "  Pass them:   --original <origTree> --refactored <refTree>" >&2
  echo "  Or register: python3 scripts/project_setup.py $PROJECT -d <checkout> \\" >&2
  echo "                   --original <o> --refactored <r>" >&2
  echo >&2
  python3 scripts/projects.py >&2
  exit 1
fi

SESSION="dfuzz-$PROJECT"
LOG="$MODULE/target/run-$PROJECT.log"

# ── relaunch into tmux unless we are already inside it ─────────────────────
if [[ $FG -eq 0 && -z "$TMUX" ]]; then
  if tmux has-session -t "$SESSION" 2>/dev/null; then
    echo "session '$SESSION' already exists. Attach with:  tmux attach -t $SESSION"
    echo "Kill it first if you want a fresh run:            tmux kill-session -t $SESSION"
    exit 1
  fi
  mkdir -p "$MODULE/target"
  tmux new-session -d -s "$SESSION" \
    "bash '${BASH_SOURCE[0]}' '$PROJECT' --original '$ORIG' --refactored '$REF' \
     --duration '$DURATION' --unit-budget '$UNIT_BUDGET' --jobs '$JOBS' $MAXARG $SRCSEEDS --fg 2>&1 | tee '$LOG'"
  echo "started tmux session '$SESSION'"
  echo "  attach : tmux attach -t $SESSION       (detach again with Ctrl-b d)"
  echo "  follow : tail -f $LOG"
  echo "  peek   : tmux capture-pane -p -t $SESSION -S -40"
  echo "  stop   : tmux kill-session -t $SESSION"
  exit 0
fi

# ── prerequisites, checked before anything is deleted ──────────────────────
[[ -f "$MODULE/tools/evosuite/evosuite-1.2.0.jar" ]] \
  || fail "EvoSuite missing — run scripts/setup_evosuite.sh"

grep -q "<id>$PROJECT</id>" "$MODULE/pom.xml" \
  || fail "pom.xml has no <profile><id>$PROJECT</id>. Maven ignores an unknown -P and still
                exits 0, so the run would compile no snapshots and report every method as a
                harness error. Register it:
                  python3 scripts/project_setup.py $PROJECT -d <checkout of the project>"

[[ -d "$ORIG" && -d "$REF" ]] || fail "source trees not found: $ORIG / $REF"

# The project's classpath. A registered project has a fat jar; the self-contained `example`
# demo needs none, so only check what the registry knows.
PROJ_JAR="$(python3 scripts/projects.py "$PROJECT" jar)"
if [[ -n "$PROJ_JAR" && ! -f "$PROJ_JAR" ]]; then
  fail "the fat jar this profile points at is gone: $PROJ_JAR
                Rebuild it: python3 scripts/project_setup.py $PROJECT -d <checkout>"
fi

# ── JDK checks. The snapshots compile at the project's own release, so a JDK older than
#    that cannot build them at all; EvoSuite 1.2.0 separately needs a Java 8 to run. ──
NEED_JAVA="$(python3 scripts/projects.py "$PROJECT" java)"
HAVE_JAVA=$(javac -version 2>&1 | sed -E 's/javac 1\.([0-9]+).*/\1/; s/javac ([0-9]+).*/\1/')
if [[ -n "$NEED_JAVA" && "$HAVE_JAVA" =~ ^[0-9]+$ ]] && (( NEED_JAVA > HAVE_JAVA )); then
  # Name a JDK that is actually installed here rather than a guessed path: the required
  # release comes from the project's bytecode and often has no JVM of exactly that number
  # (jmeter's classpath tops out at Java 9, which nobody has installed).
  SUGGEST=""
  while read -r v d; do
    (( v >= NEED_JAVA )) && { SUGGEST="$d"; break; }
  done < <(
    for d in /usr/lib/jvm/*/; do
      [[ -x "$d/bin/javac" && ! -L "${d%/}" ]] || continue     # real dirs, not the default-java symlink
      v=$("$d/bin/javac" -version 2>&1 | sed -E 's/javac 1\.([0-9]+).*/\1/; s/javac ([0-9]+).*/\1/')
      [[ "$v" =~ ^[0-9]+$ ]] && echo "$v ${d%/}"
    done | sort -n -u                                          # lowest JDK that still satisfies
  )
  fail "$PROJECT needs JDK $NEED_JAVA or newer, but javac here is $HAVE_JAVA.
                  export JAVA_HOME=${SUGGEST:-/path/to/a/jdk-$NEED_JAVA-or-newer}
                  export PATH=\$JAVA_HOME/bin:\$PATH"
fi
if ! java -version 2>&1 | grep -q '"1\.8'; then
  if [[ -z "${JAVA8_HOME:-}" ]]; then
    for c in /usr/lib/jvm/java-8-openjdk-amd64 /usr/lib/jvm/java-1.8.0-openjdk-amd64; do
      [[ -x "$c/bin/java" ]] && export JAVA8_HOME="$c" && break
    done
  fi
  [[ -n "${JAVA8_HOME:-}" ]] \
    && echo "  default java is not 8; using JAVA8_HOME=$JAVA8_HOME for EvoSuite" \
    || echo "  ! default java is not 8 and no JAVA8_HOME found; EvoSuite 1.2.0 needs one" >&2
fi

echo "################ $PROJECT — FROM SCRATCH ################"
date
echo "original    = $ORIG"
echo "refactored  = $REF"
echo "duration=$DURATION  unit-budget=${UNIT_BUDGET}s  jobs=$JOBS  ${MAXARG:-(all methods)}  java=${NEED_JAVA:-?}"
echo

echo "---- clearing generated state ----"
rm -rf "target/parsetool" "target/ast-methods.json" \
       "target/evosuite/$PROJECT" "target/seeds/$PROJECT" \
       "src/test/Dataset/$PROJECT" "src/test/resources/$PROJECT" \
       "src/test/fuzzing/$PROJECT" "src/test/resources/fuzz" \
       "reports/$PROJECT" "target/fuzz-logs/$PROJECT" \
       "target/fuzz-cwd/.cifuzz-corpus" "target/cp-$PROJECT.txt"
echo "  parser tool + AST json, evosuite suites, seed corpus, snapshots, harnesses,"
echo "  libFuzzer corpus, previous report — all removed"
echo

START=$(date +%s)
python3 run.py "$PROJECT" \
  --original "$ORIG" --refactored "$REF" \
  --duration "$DURATION" --unit-budget "$UNIT_BUDGET" --jobs "$JOBS" $MAXARG $SRCSEEDS
RC=$?
ELAPSED=$(( $(date +%s) - START ))

echo
echo "---- optional: what the unit suite covers on its own ----"
python3 scripts/unit_coverage.py "$PROJECT" 2>&1 | tail -5 || true

echo
printf '################ ELAPSED %dm%02ds  exit=%d ################\n' \
  $((ELAPSED/60)) $((ELAPSED%60)) "$RC"
echo "report: reports/$PROJECT/auto-fuzz-report.md"
echo "RUN_COMPLETE project=$PROJECT rc=$RC"
