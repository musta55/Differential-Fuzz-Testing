# Differential Fuzz Testing

Project-level differential fuzz testing for identifying semantic preservation in software
refactoring: given the **original** and the **refactored** version of
a codebase, it finds the methods whose body changed, runs the original and refactored version of
each on the **same** [Jazzer](https://github.com/CodeIntelligenceTesting/jazzer)-generated inputs,
and reports where they disagree.

- **Input:** two parallel source trees — `original/` and `refactored/` — with the same package layout.
- **Output:** a Markdown report classifying every changed method and constructor as
  **EQUIVALENT / DIVERGENT / SKIP**, with per-method branch/line coverage on *both* versions and,
  for each divergence, the reproducing input and the two differing outcomes. A SKIP carries a
  reason, so "could not be answered" never gets confused with "no difference found".

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

## Quick start (self-contained demo)

Start here: [`examples/demo/`](examples/demo/) needs no external dependencies and runs in a couple
of minutes. Its three classes are built to produce one of every verdict — an equivalent refactoring
over a 4-way branch, a dropped guard, a `List` parameter, and a refactored constructor that throws.

```bash
scripts/setup_evosuite.sh          # once: fetch EvoSuite 1.2.0 (needs a Java 8 JVM)

python3 run.py example \
    --original   examples/demo/original \
    --refactored examples/demo/refactored \
    --duration 30s
```

The report lands at `reports/example/auto-fuzz-report.md` — see [Reading the
report](#reading-the-report).

## Larger example (apex-core)

A real-project example ships under [`examples/apex-core/`](examples/apex-core/): the `original/` and
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

## What `run.py` does (the six steps)

| Step | Script | What it produces |
|------|--------|------------------|
| 1 | `build_project.py <p> --original <o> --refactored <r>` | `manifest.json` + renamed `<Class>{Original,Refactored}` snapshots |
| 2 | `prune.py <p>` | iterative `./mvnw -P<p> test-compile`; drops class pairs whose deps don't resolve |
| 3 | `gen_harnesses.py <p> <dur>` | one thin Jazzer harness per manifest method |
| 4 | `gen_unittests.py <p>` | EvoSuite unit suites for the `<Class>Original` snapshots |
| 5 | `gen_seeds.py <p>` | those tests' constants, encoded as the fuzzer's seed corpus |
| 6 | `run_project.py <p>` | fuzzes each method + differential coverage → `reports/<p>/auto-fuzz-report.md` |

Steps 1, 3, 6 are pure per-project; steps 2, 4, 5 need the project's dependencies on the classpath.
Steps 4–5 need `scripts/setup_evosuite.sh` first.
You can run steps individually, or all at once via `run.py`.

## Seeding the fuzzer with generated unit tests

The fuzzer does not start from nothing. Before fuzzing, EvoSuite generates a unit-test suite for
each `<Class>Original` snapshot, and the constants those tests use become libFuzzer's starting
corpus — so the search begins from values that already reach the code instead of growing them from
random bytes. This is part of the normal pipeline, not an option.

```bash
scripts/setup_evosuite.sh          # once: fetch EvoSuite 1.2.0 (needs a Java 8 JVM)
python3 run.py example --original examples/demo/original \
                       --refactored examples/demo/refactored
```

**How a unit test becomes a seed.** A Jazzer input is a byte string, and what those bytes mean is
decided by the engine's own argument builder — `ObjectFactory` plus Jazzer's autofuzz, including
however many bytes a receiver's constructor consumes. There is no layout to write an encoder
against. So `SeedWriter` runs that same builder against `SeedRecorder`, a `FuzzedDataProvider` that
*writes* the bytes which would have produced each value it hands out, fed with the constants
`extract_seeds.py` harvested from the suite. Every candidate is then decoded back through the real
`GenericDifferential.buildSide` and **discarded unless the arguments come back identical**, so a
seed that would have meant something other than the test it came from is never written.

It is exact where it matters: apex-core's `VersionInfo.compare` is static with two String
parameters, and its seeds decode to precisely the EvoSuite calls, e.g.
`compare("10.04.2026 @ 00:14:56 UTC", "l%")`.

Seeds are staged in `target/seeds/<project>/` and installed into `src/test/resources/` for a run,
where Jazzer's JUnit integration picks them up automatically. Both copies are generated; the
installed one is refreshed every run so a stale corpus cannot survive a regeneration.

**Limits worth knowing.** Constants are harvested by scanning literals per test case rather than by
resolving the call, so which are receiver arguments and which are method arguments is inferred:
several alignments are tried and every distinct verified one is written. A value the recorder cannot
express exactly (non-ASCII in a `String` parameter, a surrogate `char`) fails verification and is
dropped rather than silently mangled.

`scripts/unit_coverage.py` is a separate, optional tool that measures what the unit suite covers on
its own. It is the only place a JaCoCo agent is still used — Jazzer cannot measure a plain JUnit
run, because it is not in the loop when the suite executes.

## Coverage comes from Jazzer, and is differential

Every input runs through the original and the refactored method in the same iteration, so both
sides have coverage and the report shows **both**. That pairing is what makes an EQUIVALENT verdict
readable: "no divergence" is only as strong as the fraction of each version's branches the fuzzer
actually reached, and a one-sided number would hide a branch the refactoring added that nothing ever
exercised. It also makes shape changes visible — in the demo, `Grader.passes` reports `4/4` branches
on the original against `2/2` on the refactored, because the refactoring deleted a guard.

The numbers are Jazzer's own instrumentation, which it installs to steer its search, so nothing is
counted that the fuzzer did not drive. `JazzerCoverage` dumps it at JVM exit in JaCoCo `.exec`
format and `CovReport` reads it with the jacoco-core **library**.

**There is no JaCoCo agent in this pipeline.** Two agents instrumenting the same classes made Jazzer
record jacoco-rewritten bytes whose class ids no longer matched the class files, and every method
reported `0/0`. Jazzer's numbers are also better attributed: it correctly reports a method the
harness never calls as `0/1`, where the JaCoCo agent said `1/1`.

Two footnotes on Jazzer, both found the hard way:

- `-Djazzer.coverage_dump` is **silently inert** under `mvn test`. Jazzer only acts on it in
  `FuzzTargetRunner.shutdown()`, which the JUnit integration never calls, so `JazzerCoverage`
  registers the dump as a shutdown hook itself.
- Surefire's `<argLine>` configuration element beats a command-line `-DargLine`, so the pom exposes
  a `${fuzz.jvmArgs}` property as the supported seam for passing test-JVM flags.

## Reading the report

`reports/<project>/auto-fuzz-report.md` opens with the class and method counts and a summary table,
then one row per method. From the demo:

| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | Branch ref | Confidence | Why |
|---|---|---|---|---:|---:|---|---|---|---|
| `Grader.grade` | scalar | **EQUIVALENT** | - | 42,134 | 42,134 | 6/6 | 6/6 | all 12 branches exercised | no divergence found in 30s of fuzzing |
| `Grader.passes` | scalar | **DIVERGENT** | - | 7 | 7 | 4/4 | 2/2 | witnessed | **exception type** — on `[-5962]` original throws IllegalArgumentException, refactored returns false |
| `Grader.total` | object | **EQUIVALENT** | - | 40,001 | 40,001 | 3/4 | 3/4 | 6/8 branches (75%) | no divergence found in 30s of fuzzing |
| `Simple.foo` | scalar | **SKIP** | never ran | 695,451 | 0 | 0/0 | 0/0 | - | refactored: neither autofuzz nor constructor synthesis built `SimpleRefactored` |

**How much testing the budget bought.** `Inputs` is every input the fuzzer produced in the fixed
`--duration`; `Compared` is only those that got as far as invoking **both** versions. The summary
totals both. The gap is the point: `Simple.foo` above tried 695,451 inputs and completed **zero**
comparisons, because the refactored receiver cannot be constructed — the fuzzer worked hard and
learned nothing. Neither number is visible in surefire's "Tests run", which counts JUnit invocations
(each seed once, plus a single call for the whole fuzzing session), so the engine counts them itself
and prints them at JVM exit. A DIVERGENT usually shows a small count because the first witnessed
mismatch stops the run.

**There are exactly three verdicts.**

- **DIVERGENT** — the two versions differ on some input, in exception type, return value, or receiver
  state after the call. The `Why` column names which of those differed and prints the reproducing
  input. This is the finding the tool exists to surface; triage each as bug vs intentional fix.
- **EQUIVALENT** — no divergence found *in the budget*. Never a proof; read the Confidence column.
- **SKIP** — no verdict could be reached. The `Reason` column says which kind, and the summary
  totals them under **Why the SKIPs**:

| Reason | What it means |
|---|---|
| `never ran` | the harness completed but no input ever built a receiver **and** arguments for **both** sides, so nothing was compared. The `Why` column names the side that refused. Usually a fact about the refactoring — in the demo, `Simple.foo` lands here because the *refactored constructor throws*. **Not** equivalence. |
| `structurally untestable` | no input could ever work: an abstract receiver with no concrete subtype, or a parameter type nothing can build. |
| `pruned snapshot` | the pair did not compile and was dropped by the compile gate. |
| `harness error` | missing class at runtime, inaccessible member, bad manifest entry. |
| `sanitizer finding` | it *did* run on both sides without diverging, but a Jazzer sanitizer fired on the code itself. Worth reading — it is a bug report about the code, just not a differential result. |

Only EQUIVALENT and DIVERGENT are evidence about behaviour; the summary says how many of the
manifest's methods got that far.

**The Confidence column is how much an EQUIVALENT is worth.** Fuzzing is sound for non-equivalence
and incomplete for equivalence, so "no counterexample in 30 seconds" needs a second number to be
readable. Confidence reports the branches actually reached across both versions: `all 12 branches
exercised` is a result worth trusting, `only 2/8 branches (25%) — weak` is barely evidence at all.
Anything under 60% is listed again under **EQUIVALENT verdicts to distrust** at the end of the
report, with the suggested remedies (raise `--duration`, improve the seed corpus).

To see a divergence's proof:

```bash
grep -a -A6 -F "[DIFFERENTIAL MISMATCH]" target/fuzz-logs/<project>/<Class>_<method>.log
```

The block prints `methodArgs` (the reproducing input, non-printables escaped) and both outcomes.
Use `grep -a`: fuzz logs contain raw bytes, and without it grep treats them as binary and silently
prints nothing.

## Regression suite for reported bugs

```bash
python3 scripts/check_issues.py            # 5 cases, ~4 minutes at 15s/method
python3 scripts/check_issues.py --case issue1-wrapped-exception
```

Each case in [`examples/issues/`](examples/issues/) is a minimal `original`/`refactored` pair with a
known correct outcome, run through the real pipeline. It asserts **two** things per case — how many
changed methods the differ found, and the verdict the fuzzer reached — because the bugs failed at
two different stages: some produced a wrong verdict, one produced no verdict at all because the
method was never recognised as changed. Checking verdicts alone would have scored that last one as
"no failures".

| Case | Asserts |
|---|---|
| `issue1-wrapped-exception` | same outer `Error`, different wrapped cause → DIVERGENT |
| `issue2-empty-list` | a `List<Integer>` parameter is populated, not empty → DIVERGENT |
| `issue3-string-literal` | a method whose only change is a string literal is still found → DIVERGENT |
| `guard-comment-only` | a comment-only edit still counts as unchanged → 0 methods |
| `guard-same-cause` | identical cause chains stay EQUIVALENT (no false positive) |

The two `guard-*` cases matter as much as the rest: they stop a fix from "passing" by simply
reporting more differences than it should.

Every case writes a full differential report to `reports/issues/<case>.md`, indexed by
`reports/issues/SUMMARY.md`, which pairs each observed outcome with the expected one — the per-case
report says what the fuzzer concluded, only the summary says whether the reported bug is gone.

The cases run **unseeded** (the seed corpus is cleared first, so no report can credit seeds it never
used); they are behavioural assertions, not a seeding benchmark.

> It builds into the `example` profile's directories, so it overwrites whatever project was built
> there last. Rebuild yours afterwards.

## What counts as a changed method

A method enters the manifest when its body differs. Comments are ignored — a reworded javadoc must
not mark every method in a file as changed — but **string and character literals are compared**, so
`return "alpha"` vs `return "beta"` is a real change and gets fuzzed. (Literals used to be ignored
too, which silently produced `0 changed methods` for such a pair; that was github issue #3.)

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
  gen_unittests.py                       (4) EvoSuite suites for the Original snapshots
  gen_seeds.py                           (5) extract constants + encode them as a seed corpus
  extract_seeds.py                       (5a) per-test-case constants -> seed-values.json
  run_project.py                         (6) fuzz + differential coverage -> report
  unit_coverage.py                       optional: what the unit suite alone covers, per method
  auto_select.py                         shared Java-parsing helpers
  CovReport.java                         per-method branch/line extractor (reads Jazzer's .exec)
  setup_deps.sh                          install a project's built classes as local Maven jars
  setup_evosuite.sh                      fetch EvoSuite + install its runtime locally
src/test/java/fuzz/auto/
  GenericDifferential.java               the shared reflection engine + oracle
  ObjectFactory.java                     builds any argument: scalars -> autofuzz -> ctor synthesis
  ReplayProvider.java                    deterministic provider so both sides get identical inputs
  SeedRecorder.java                      its inverse: writes the bytes that decode to given values
  SeedWriter.java                        unit-test constants -> verified Jazzer seed corpus
  JazzerCoverage.java                    dumps Jazzer's own coverage as a JaCoCo .exec at exit
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

- the **exception** differs — class *and* cause chain, so `Error(NullPointerException)` and
  `Error(UnsupportedOperationException)` are not equal (messages are never compared: fuzzed messages
  embed the input and would make almost everything divergent), or
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
