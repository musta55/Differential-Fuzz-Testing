# Differential Fuzz Testing

Project-level differential fuzz testing for identifying semantic preservation in software
refactoring: given the **original** and the **refactored** version of
a codebase, it finds the methods whose body changed, runs the original and refactored version of
each on the **same** [Jazzer](https://github.com/CodeIntelligenceTesting/jazzer)-generated inputs,
and reports where they disagree.

- **Input:** two parallel source trees — `original/` and `refactored/` — with the same package layout.
- **Output:** a Markdown report classifying every changed method and constructor as
  **EQUIVALENT / DIVERGENT / FINDING / NEVER-RAN / SKIP / ERROR**, with per-method branch/line
  coverage and, for each divergence, the reproducing input and the two differing outcomes.

If for even one input the two versions differ — in return value, exception type, or the state of
the receiver they were called on — the method is **DIVERGENT**: either a bug the refactoring
introduced, or an intentional fix. That is the finding the tool exists to surface.

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
**every method whose body changed**, plus changed constructors.

Arguments are built in three tiers: scalars (primitives/boxes, `String`, primitive arrays) direct
from fuzz bytes; anything else via Jazzer's `Autofuzz.consume`; and, when autofuzz declines, by
recursive constructor synthesis. Methods taking domain objects are therefore **in** scope — on
apex-core that is the difference between 83 and 159 testable methods, since a scalar-only filter
discarded exactly half the changed methods before fuzzing began.

What still cannot be reached: a method whose **receiver** is an abstract class or interface. The
snapshots are renamed copies (`Foo` → `FooOriginal`), which severs them from their own subclass
hierarchy, so no concrete subtype of the snapshot exists to instantiate. Those are reported SKIP
with that reason.

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

- **DIVERGENT** — the two versions differ on some input, in exception type, return value, or
  receiver state after the call. The real finding; triage each as bug vs intentional fix. The
  `reason` line names what differed, down to the field (`readWriteRoles: TreeSet[B] vs TreeSet[]`).
  To see the proof:
  ```bash
  grep -a -A6 -F "[DIFFERENTIAL MISMATCH]" target/fuzz-logs/<project>/<Class>_<method>.log
  ```
  The block prints `methodArgs` (the reproducing input, with non-printables escaped) and both
  outcomes. Use `grep -a`: fuzz logs contain raw bytes, and without it grep treats them as binary
  and silently prints nothing.
- **EQUIVALENT** — no divergence found in the time budget. Trust it in proportion to the **Branch**
  column: `4/4` = both outcomes of every decision were exercised; `0/0` = branchless (read the Line
  column); low branch = under-explored, not proven equivalent.
- **NEVER-RAN** — the harness completed but the receiver or arguments were never successfully
  built, so nothing was ever compared. This is **not** equivalence; it is an untested method that
  would otherwise have been miscounted as a clean result.
- **SKIP** — structurally untestable: an abstract/interface receiver with no concrete subtype, a
  parameter type nothing can build, or a snapshot dropped by pruning.
- **ERROR** — ran but couldn't be tested (missing class at runtime, inaccessible member, bad
  manifest entry).

Only EQUIVALENT, DIVERGENT and FINDING mean both versions actually executed. The report's summary
line states how many of the manifest's methods that was.

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
src/test/java/fuzz/auto/
  GenericDifferential.java               the shared reflection engine + oracle
  ObjectFactory.java                     builds any argument: scalars -> autofuzz -> ctor synthesis
  ReplayProvider.java                    deterministic provider so both sides get identical inputs
  Digest.java                            structural comparison of return values and receiver state
examples/apex-core/{original,refactored}/          worked example input trees
examples/apex-core/sample-report.md                what the output looks like
```

`src/test/Dataset/`, `src/test/fuzzing/`, `src/test/resources/`, `reports/`, and `target/` are all
generated by the pipeline and git-ignored — regenerate them with `run.py`.

## How the oracle decides (and its limits)

The engine captures the iteration's fuzz bytes once and **replays them into two independent
builds**, so the original and the refactored side receive structurally identical arguments that
share no references. That matters as soon as arguments are objects: an object graph cannot be
deep-copied generically, and handing both sides the same mutable instance would let the first call
mutate it and make the second observe a different input.

Each side is invoked via reflection on a **3-second per-call watchdog** (a runaway input becomes a
TIMEOUT, not a stall). DIVERGENT iff:

- **exception type** differs, or
- the **return value** differs — by value for scalars/JDK types, otherwise by a structural field
  walk, because domain classes inherit identity `equals()` and would diverge on every input, or
- **receiver state after the call** differs, and only when the two receivers started out equal.

A one-sided TIMEOUT is inconclusive, never divergent.

### What the structural comparison deliberately ignores

Comparing object state naively reports a divergence for almost every real refactoring, so the
walk excludes four things that are representation rather than behaviour:

- **Fields present on only one side.** Changing internal representation *is* refactoring:
  apex-core's `RoundRobinRefactored` adds `index` and `nodeList` fields the original lacks. Only
  fields both versions declare are compared.
- **Ambient JVM state.** A field walk follows references wherever they lead — a `ThreadGroup`
  reaches its parent and thus every live thread in the process. `Thread`, `ThreadGroup`,
  `ClassLoader`, executors and loggers are never entered.
- **Identity-equality value types.** `AtomicInteger` and friends are `Number`s using `Object`'s
  `equals()`; two counters both holding 1 are equal here, compared by rendered value.
- **Known-nondeterministic field names** (timestamps, hashes, `random`), listed in `Digest`.

The class name in a digest has its `Original`/`Refactored` suffix stripped, and map/set entries are
sorted, since `HashMap` iteration order depends on identity hash codes.

### Remaining limits

- **Sound for non-equivalence, incomplete for equivalence.** A DIVERGENT is a real witness;
  EQUIVALENT only means "no divergence found in the budget".
- **Abstract receivers are unreachable** (see the input contract above) and reported SKIP.
- **Signature-changing refactorings** are out of scope: a changed parameter type surfaces as ERROR,
  a changed arity means the methods never pair and the method is dropped.
- **Coverage rewards new edges, not new disagreements**, so a value-only divergence with identical
  control flow is found only by luck.
