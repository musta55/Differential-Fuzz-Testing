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

## Run it on your own project

Nothing about a target project lives in a script. One command builds the project, generates its
Maven profile and records it; after that the pipeline knows it by name.

```bash
# 0. Once per project: build it, shade its modules and their dependencies into one jar,
#    write the -P<name> profile into pom.xml, record both trees in projects.json.
python3 scripts/project_setup.py deltaspike \
    -d           /path/to/RefAgent-reproduce/projects/before/deltaspike \
    --original   /path/to/RefAgent-reproduce/projects/before/deltaspike \
    --refactored /path/to/RefAgent-reproduce/projects/after/deltaspike

# 1. Smoke-test the wiring before committing hours to it.
scripts/run_tmux.sh deltaspike --max 5 --duration 15s --unit-budget 20

# 2. The real run.
scripts/run_tmux.sh deltaspike
```

`-d` is any checkout of the project, and it only supplies the **classpath** — the classes the
snapshots reference. `--original` / `--refactored` are the two trees actually diffed and fuzzed.
They are usually the same before/after pair, and `-d` normally points at the original.

**What step 0 does.** It runs the project's own build (`mvnw`/`mvn`, `gradlew`/`gradle`, or plain
`javac` when there is no build file), collects every jar module in the reactor and shades them plus
their transitive third-party dependencies into one jar, then splices a profile into
[`pom.xml`](pom.xml) pointing at it. Maven resolution does what used to be manual: the hand-written
apex-core profile lists ten third-party libraries by name purely because `setup_deps.sh` installed
bare module jars with no pom, so nothing was transitive.

It is idempotent — re-run it whenever the project is rebuilt. The generated profile sits between
`BEGIN/END GENERATED PROFILE` comments and is replaced in place; the rest of `pom.xml`, hand-written
profiles included, is untouched. (The edit is textual on purpose: an XML round-trip drops every
comment in the file.)

**Committing the profile is safe.** The fat jar is symlinked to `tools/fatjars/<name>.jar`
(git-ignored) and the profile points at `${project.basedir}/tools/fatjars/<name>.jar`, so the
generated XML has no machine-specific path in it and is the same for everyone. A symlink rather
than a copy — openmeetings' jar is 305 MB. To retire a project,
`python3 scripts/project_setup.py <name> --remove` deletes its profile block and registry entry.

**The registry.** `projects.json` at the repo root records each project's two trees, its fat jar and
its JDK release. It is machine-local and git-ignored.

```bash
scripts/run_tmux.sh --list                       # everything registered
python3 run.py deltaspike --max 5                # trees come from the registry
python3 run.py deltaspike --original <o> --refactored <r>   # or override them
```

**A partial build is fine.** Big projects have modules the fuzzer has no use for and that do not
build here — skywalking's `apm-webapp` shells out to `npm ci`. The install runs `--fail-at-end`,
modules that produced no jar are dropped from the shade with a line saying which, and the run
continues. Use `--build-args` to steer the project's own build
(`--build-args='-pl !apm-webapp'`), and `--jar` to reuse a fat jar you already have.

**JDK.** The profile compiles the snapshots at the release step 0 read out of the built jar's
class-file version, so a Java 11 project gets `maven.compiler.source=11` with no editing. You still
have to *run* on a JDK at least that new; `run_tmux.sh` checks this before deleting anything and
prints the `export JAVA_HOME=…` line if it does not hold. EvoSuite 1.2.0 separately needs a Java 8
to launch — set `JAVA8_HOME` (`run_tmux.sh` finds the usual locations itself).

```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64   # skywalking is a Java 11 project
export JAVA8_HOME=/usr/lib/jvm/java-8-openjdk-amd64   # EvoSuite needs 8, whatever the project is
```

**Past Java 8, add `--source-seeds`.** Those two JDK requirements collide. EvoSuite 1.2.0 only runs
on a Java 8 JVM, and a Java 8 JVM cannot load a snapshot compiled at class-file 53 or newer — so on
any project built past Java 8, step 4 detects the mismatch and skips itself:

```
SKIPPING EvoSuite for openmeetings: snapshots are class-file 61 (Java 17) but the EvoSuite
JVM .../java-8-openjdk-amd64/bin/java only loads up to 52 (Java 8)
```

That is deliberate — EvoSuite on a newer JVM hangs rather than failing — and the run continues, but
**the fuzzer then starts from an empty corpus**. `--source-seeds` mines the constants out of the
snapshot sources instead, which needs no JVM of any particular version:

```bash
scripts/run_tmux.sh openmeetings --source-seeds          # Java 17: the only way to get seeds
```

It is opt-in rather than automatic on purpose: a corpus mined from sources is a different kind of
corpus from one generated by EvoSuite, and swapping between them silently would make one project's
divergence rate incomparable with another's. Of the projects below only `deltaspike` (Java 8) gets
an EvoSuite corpus; `skywalking`, `jmeter` and `openmeetings` all need this flag.

### Verified on

Registered and run from the `before/`+`after/` trees of
[RefAgent-reproduce](https://github.com/musta55/RefAgent-reproduce/tree/track-refactored-projects),
none of which the tool previously had an entry for:

| Project | Build system | JDK | Fat jar | Changed methods | What it exercised |
|---|---|---|---|---:|---|
| `deltaspike` | Maven, **no wrapper** | 8 | 34 modules, 12 MB | 167 → 119 after prune | the `mvn` fallback when a project ships no `mvnw` |
| `openmeetings` | Maven, no wrapper | 17 | 9 modules, 305 MB | 311 → 178 after prune | the JDK path — profile written at 17, not the base pom's 8 |
| `skywalking` | Maven via `mvnw` | 11 | 71 modules, 117 MB | 87 changed files | the wrapper, and surviving a module that fails (`apm-webapp` runs `npm ci`) |
| `jmeter` | **Gradle** via `gradlew` | 9 | 80 MB | 456 changed files | the Gradle path; needed `--build-args='-PchecksumIgnore'` |

Two were taken all the way to a report with `--max 5 --duration 15s`:

- **deltaspike** (Java 8, EvoSuite-seeded): 5 methods, 119,385 inputs, 117,512 differential
  comparisons, all EQUIVALENT.
- **openmeetings** (Java 17, unseeded): 2 EQUIVALENT, 2 SKIP, and **1 DIVERGENT** —
  `LdapOptions.ctor` on an empty `Properties` leaves `useAdminForAttrs` `false` on the original and
  `true` on the refactored version. A real behavioural change, on a project the tool had no entry
  for an hour earlier.

The bundled `example` still runs through `run_tmux.sh` in **1m37s** and reproduces both of its
DIVERGENTs, so none of this changed a verdict.

The JDK column is read out of each fat jar's class-file versions, exactly. It is a maximum over the
whole classpath, which is what decides whether `javac` can read it: `jmeter`'s own classes are Java
8, but six Jetty ALPN classes among its 46,470 are Java 9, so the profile is written at 9.

Whatever a project's size, start with `--max 5`: it bounds the EvoSuite step as well as the fuzzing,
so the wiring is confirmed in minutes rather than hours.

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

# 2. Run the whole pipeline. The bundled trees are pre-seeded in the registry, so the
#    two --original/--refactored flags are optional here.
python3 run.py apex-core

# Smoke-test the first few methods only:
python3 run.py apex-core --max 5
```

The report lands at `reports/apex-core/auto-fuzz-report.md`.

> **Requirements:** a JDK and Maven for the fuzzing steps. apex-core is a Java 8 project — build and
> run it on JDK 8. Step 1 (`build_project.py`) is pure Python.

`apex-core` is the one profile still written by hand, kept as a worked example of what
`project_setup.py` now generates: `setup_deps.sh` installs four bare module jars, and because those
carry no pom, the profile has to name ten third-party libraries itself. To convert it, delete the
hand-written profile from `pom.xml` and run
`python3 scripts/project_setup.py apex-core -d /path/to/apex-core` instead. `setup_deps.sh` is
needed only for the hand-written profile; no other project uses it.

## The pipeline, step by step

| Step | Script | What it produces |
|------|--------|------------------|
| 0 | `project_setup.py <p> -d <dir>` | the project's fat jar, its `-P<p>` profile in `pom.xml`, its `projects.json` entry — **once per project**, not part of `run.py` |
| 1 | `build_project.py <p> --original <o> --refactored <r>` | `manifest.json` + renamed `<Class>{Original,Refactored}` snapshots |
| 2 | `prune.py <p>` | iterative `./mvnw -P<p> test-compile`; drops class pairs whose deps don't resolve |
| 3 | `gen_harnesses.py <p> <dur>` | one thin Jazzer harness per manifest method |
| 4 | `gen_unittests.py <p>` | EvoSuite unit suites for the `<Class>Original` snapshots |
| 5 | `gen_seeds.py <p>` | those tests' constants, encoded as the fuzzer's seed corpus |
| 6 | `run_project.py <p>` | fuzzes each method + differential coverage → `reports/<p>/auto-fuzz-report.md` |

Steps 1, 3, 6 are pure per-project; steps 2, 4, 5 need the project's dependencies on the classpath,
which is what step 0 provides. Steps 4–5 need `scripts/setup_evosuite.sh` first.
You can run steps individually, all at once via `run.py`, or from scratch and detached via
`scripts/run_tmux.sh`. `run.py --setup <dir>` folds step 0 in.

**Step 2 fails loudly now.** Maven treats an unknown `-P` as a warning and exits 0, so a project
with no profile used to sail through a `test-compile` that built nothing, and every method came out
`harness error` with nothing saying why. `prune.py` checks the profile exists before it runs and
that the compile actually produced snapshot classes after it.

## Seeding the fuzzer with generated unit tests

The fuzzer does not start from nothing. Before fuzzing, EvoSuite generates a unit-test suite for
each `<Class>Original` snapshot, and the constants those tests use become libFuzzer's starting
corpus — so the search begins from values that already reach the code instead of growing them from
random bytes. This is part of the normal pipeline, not an option.

```bash
scripts/setup_evosuite.sh          # once: fetch EvoSuite 1.2.0 (needs a Java 8 JVM)
python3 run.py example
```

On a project built past Java 8 this step cannot run at all — EvoSuite 1.2.0 needs a Java 8 JVM and
a Java 8 JVM cannot load the snapshots — so it detects the mismatch, skips itself, and the fuzzer
starts unseeded unless you pass `--source-seeds`. See [Run it on your own
project](#run-it-on-your-own-project).

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

## How methods are found: Using Java AST

Method discovery, the changed/unchanged decision and parameter classification all happen in
[`scripts/MethodExtractor.java`](scripts/MethodExtractor.java), which parses both trees with
**[JavaParser](https://javaparser.org/) 3.28.2**. It is fetched from Maven Central on first use and
compiled on demand, like `CovReport.java`; it is deliberately *not* a pom dependency, so it never
reaches the fuzzing classpath.

**Syntactic only, on purpose.** JavaParser is used without symbol resolution. If symbol resolution were turned on, JavaParser would throw errors or fail to parse whenever it encountered an unknown class or unresolved method, these are alseady being done later in the pruning setps. These trees routinely
reference types that are on no classpath yet — the whole point of the later `prune` step is that
some pairs do not compile — so anything needing resolution would fail on exactly the inputs this has
to handle. Everything required here is syntax: which methods exist, what their bodies are, and
whether the two sides differ.

**A method is changed when its comment-free AST differs.** Printing each body through JavaParser
with comments disabled makes the comparison immune to reformatting and comment edits, while
preserving literals — so `return "alpha"` vs `return "beta"` is a real change. Signatures are
compared too, so a same-arity parameter-type change is checked as well.


- **Any method preceded by an annotation with a string argument was silently dropped.** The regex
  matched `@SuppressWarnings("...")` as a method whose `params` group ran past its own closing paren
  into the *real* signature below it; since `finditer` does not revisit consumed text, that method
  was never seen again. On apex-core this cost **three genuinely changed methods**, including two
  rewritten `toArray` implementations — never fuzzed, and never reported as missing.
- **Literal-only changes were invisible** (github issue #3), because literals had to be blanked to
  keep `{` inside a string from breaking brace matching.

A parser answers all of that by construction. Switching cost nothing on the existing corpora: the
demo produces byte-identical verdicts, and apex-core went from 83 to **86** changed methods — the
three the regex had been swallowing, with none lost.

## Layout

```
run.py                                   one entry point: two trees -> report
pom.xml                                  engine-only base; one <profile> per target project
projects.json                            the registry: per project, its trees/jar/JDK (git-ignored)
scripts/
  project_setup.py                       (0) build a project -> fat jar + pom profile + registry entry
  projects.py                            the registry itself; `python3 scripts/projects.py` lists it
  run_tmux.sh                            from-scratch detached run of the whole pipeline
  build_project.py                       (1) two-tree diff -> manifest + snapshots
  prune.py                               (2) compile-and-drop gate
  gen_harnesses.py                       (3) one Jazzer harness per method
  gen_unittests.py                       (4) EvoSuite suites for the Original snapshots
  gen_seeds.py                           (5) extract constants + encode them as a seed corpus
  extract_seeds.py                       (5a) per-test-case constants -> seed-values.json
  source_seeds.py                        (5b) --source-seeds: same, mined from the snapshot sources
  run_project.py                         (6) fuzz + differential coverage -> report
  unit_coverage.py                       optional: what the unit suite alone covers, per method
  MethodExtractor.java                   (1a) JavaParser AST: find + diff + classify methods
  auto_select.py                         package lookup + top-level type spans (raw-text helpers)
  CovReport.java                         per-method branch/line extractor (reads Jazzer's .exec)
  setup_deps.sh                          legacy: local Maven jars for the hand-written apex-core profile
  setup_evosuite.sh                      fetch EvoSuite + install its runtime locally
src/test/java/fuzz/auto/
  GenericDifferential.java               the shared reflection engine + oracle
  ObjectFactory.java                     builds any argument: scalars -> autofuzz -> ctor synthesis
  ReplayProvider.java                    deterministic provider so both sides get identical inputs
  SeedRecorder.java                      its inverse: writes the bytes that decode to given values
  SeedWriter.java                        unit-test constants -> verified Jazzer seed corpus
  JazzerCoverage.java                    dumps Jazzer's own coverage as a JaCoCo .exec at exit
  Digest.java                            structural comparison of return values and receiver state
tools/fatjars/<project>.jar              symlink to each registered project's fat jar (git-ignored)
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
