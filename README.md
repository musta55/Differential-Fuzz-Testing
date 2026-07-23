# Differential Fuzz Testing

Project-level differential fuzz testing for identifying semantic preservation in software
refactoring: given the **original** and the **refactored** version of
a codebase, it finds the methods whose body changed, runs the original and refactored version of
each on the **same** [Jazzer](https://github.com/CodeIntelligenceTesting/jazzer)-generated inputs,
and reports where they disagree.

- **Input:** two parallel source trees — `original/` and `refactored/` — with the same package layout.
- **Output:** a Markdown report classifying every changed, auto-fuzzable method as
  **EQUIVALENT / DIVERGENT / SKIP / ERROR**, with per-method branch/line coverage and, for each
  divergence, the reproducing input and the two differing outcomes.

If for even one input the two versions differ (different return value, or different exception type),
the method is **DIVERGENT** — either a bug the refactoring introduced, or an intentional fix. That
is the finding the tool exists to surface.

This is a standalone extraction of the differential-fuzzing work from the RefAgent replication
project. It has no dependency on RefAgent output or that repo's layout — you bring any
`original/` + `refactored/` pair.

## Input contract

```
original/                          refactored/
  com/acme/Foo.java                  com/acme/Foo.java
  com/acme/util/Bar.java             com/acme/util/Bar.java
```

Files are paired by their path relative to each tree root. Only files present in **both** trees
are considered (a brand-new class has no original to diff against). For each pair the tool keeps
the methods whose body changed **and** whose parameters are all scalars it can synthesize from fuzz
bytes — primitives/boxes, `String`, and primitive arrays. Methods taking complex domain objects
are out of scope (excluded automatically).

## Quick start (apex-core example)

A worked example ships under [`examples/apex-core/`](examples/apex-core/): the `original/` and
`refactored/` trees for 27 apex-core classes, plus a [`sample-report.md`](examples/apex-core/sample-report.md).

```bash
# 1. Provide the target project's own dependencies (apex-core needs the other apex classes).
#    The argument is a SEPARATE, already-built apex-core source checkout (mvn install -DskipTests) —
#    it must contain api/, common/, bufferserver/, engine/ each with a built target/classes.
#    This is NOT examples/apex-core (those are just the diff snapshots the tool fuzzes).
#    It installs the four modules as org.apache.apex.local:apex-*:3.7.0-local for the -Papex-core profile.
#    (First run only: chmod +x scripts/setup_deps.sh, or invoke it as `bash scripts/setup_deps.sh ...`.)
scripts/setup_deps.sh /path/to/built/apex-core

# 2. Run the whole pipeline: two trees in, report out.
python3 run.py apex-core \
    --original   examples/apex-core/original \
    --refactored examples/apex-core/refactored

# Smoke-test the first few methods only:
python3 run.py apex-core --original examples/apex-core/original \
    --refactored examples/apex-core/refactored --max 5
```

The report lands at `reports/apex-core/auto-fuzz-report.md`.

> **Requirements:** a JDK and Maven for the fuzzing steps. apex-core is a Java 8 project — build and
> run it on JDK 8. A Java 17 project must be built/run on JDK 21 (`export JAVA_HOME=...`); set its
> profile's `<maven.compiler.source|target>` to match. Step 1 (`build_project.py`) is pure Python.

## What `run.py` does (the four steps)

| Step | Script | What it produces |
|------|--------|------------------|
| 1 | `build_project.py <p> --original <o> --refactored <r>` | `manifest.json` + renamed `<Class>{Original,Refactored}` snapshots |
| 2 | `prune.py <p>` | iterative `mvn -P<p> test-compile`; drops class pairs whose deps don't resolve |
| 3 | `gen_harnesses.py <p> <dur>` | one thin Jazzer harness per manifest method |
| 4 | `run_project.py <p>` | fuzzes each method + JaCoCo coverage → `reports/<p>/auto-fuzz-report.md` |

Steps 1, 3, 4 are pure per-project; step 2 needs the project's dependencies on the classpath.
You can run steps individually, or all at once via `run.py`.

## Reading the report

- **DIVERGENT** — the two versions differ on some input (exception type or return value). The real
  finding; triage each as bug vs intentional fix. To see the proof:
  ```bash
  grep -A5 -F "[DIFFERENTIAL MISMATCH]" target/fuzz-logs/<project>/<Class>_<method>.log
  ```
  The block prints `methodArgs` (the reproducing input) and both `original` / `refactored` outcomes.
- **EQUIVALENT** — no divergence found in the time budget. Trust it in proportion to the **Branch**
  column: `4/4` = both outcomes of every decision were exercised; `0/0` = branchless (read the Line
  column); low branch = under-explored, not proven equivalent.
- **SKIP** — instance method whose receiver couldn't be constructed generically (abstract/interface,
  or no usable constructor).
- **ERROR** — ran but couldn't be tested (e.g. the constructor throws at runtime).

## Add your own project

1. Put your two source trees anywhere (e.g. `examples/<project>/{original,refactored}`).
2. Make the project's dependencies resolvable — add a `<profile id="<project>">` in
   [`pom.xml`](pom.xml) (copy the `apex-core` profile: change the id, list your project's
   dependencies, and point the two `add-test-source` paths at `src/test/Dataset/<project>` and
   `src/test/fuzzing/<project>`).
3. `python3 run.py <project> --original <o> --refactored <r>`. `prune.py` auto-drops whatever
   won't compile.

## Layout

```
run.py                                   one entry point: two trees -> report
pom.xml                                  engine-only base; one <profile> per target project
scripts/
  build_project.py                       (1) two-tree diff -> manifest + snapshots
  prune.py                               (2) compile-and-drop gate
  gen_harnesses.py                       (3) one Jazzer harness per method
  run_project.py                         (4) fuzz + coverage -> report
  auto_select.py                         shared Java-parsing helpers
  CovReport.java                         JaCoCo per-method branch/line extractor
  setup_deps.sh                          install a project's built classes as local Maven jars
src/test/java/fuzz/auto/GenericDifferential.java   the shared reflection engine + oracle
examples/apex-core/{original,refactored}/          worked example input trees
examples/apex-core/sample-report.md                what the output looks like
```

`src/test/Dataset/`, `src/test/fuzzing/`, `src/test/resources/`, `reports/`, and `target/` are all
generated by the pipeline and git-ignored — regenerate them with `run.py`.

## How the oracle decides (and its limits)

The engine builds the receiver and arguments from Jazzer's `FuzzedDataProvider`, invokes both
versions via reflection on a **3-second per-call watchdog** (a runaway input becomes a TIMEOUT, not
a stall), and compares. Different **exception type**, or different scalar/JDK **return value**, =
DIVERGENT. A one-sided TIMEOUT is treated as inconclusive, not divergent.

The oracle is deliberately shallow: it compares return value + exception *type* only — not exception
messages, deep object equality, or side effects. Fuzzing is nondeterministic, so the authoritative
record of a divergence is the report row plus the log line still containing `[DIFFERENTIAL MISMATCH]`;
`methodArgs` lets you reproduce it by hand.
