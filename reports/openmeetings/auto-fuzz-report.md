# Differential-Fuzzing Report — openmeetings

Generated 2026-09-15 17:12:49 · mode **fuzz** · **15s** per method · seed corpus **0** files · carried-over corpus **cleared**

- original tree: `/home/mhasan02/2ndSem/RefAgent-reproduce/projects/before/openmeetings`
- refactored tree: `/home/mhasan02/2ndSem/RefAgent-reproduce/projects/after/openmeetings`

## Summary

**4 classes · 5 changed methods**

| Outcome | Count | Meaning |
|---|---:|---|
| **EQUIVALENT** | 2 | no input made the two versions differ within the budget — trust it in proportion to the Confidence column |
| **DIVERGENT** | 1 | a concrete input makes them differ: a real behavioural change |
| **SKIP** | 2 | no verdict could be reached — see the breakdown below |

**3 of 5 methods got a verdict**; 2 could not be processed. Only EQUIVALENT and DIVERGENT are evidence about behaviour.

**Testing effort:** every method got a fixed budget of **15s**, in which the fuzzer generated **22,867 inputs** and completed **10,291 differential comparisons** (both versions invoked on the same input). The per-method counts are in the table below.

> `Inputs` is every input the fuzzer produced; `Compared` is only those that got as far as running BOTH versions. A large gap means most inputs died building a receiver or an argument, which is exactly when an EQUIVALENT should be doubted — and it is invisible in surefire's "Tests run", which counts JUnit invocations (each seed once, plus one call for the whole fuzzing session), not fuzz iterations.

### Why the SKIPs

| Reason | Count | What it means |
|---|---:|---|
| structurally untestable | 1 | no input could ever work: an abstract receiver with no concrete subtype, or a parameter type nothing can build |
| never ran | 1 | the harness completed but no input ever built a receiver **and** arguments for **both** sides, so nothing was ever compared. Usually a fact about the refactoring — e.g. the refactored constructor throws. This is **not** equivalence |

> Fuzzing is **sound for non-equivalence and incomplete for equivalence**: a DIVERGENT is a witnessed fact, an EQUIVALENT is only "no counterexample found in the budget". That is what the Confidence column is for — it reports how much of the two versions the fuzzer actually reached, so an EQUIVALENT at 0/8 branches can be told apart from one at 8/8.

## Per method

Coverage is **differential**: `orig` is the `<Class>Original` snapshot, `ref` the `<Class>Refactored` one, both measured by Jazzer in the same run.

| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | Branch ref | Line orig | Line ref | Confidence | Why |
|---|---|---|---|---:|---:|---|---|---|---|---|---|
| `ImageConverter.convertImage` | object | **SKIP** | structurally untestable | 2 | 0 | 0/0 | 0/0 | 0/1 | 0/1 | - | parameter type has no buildable form: org.apache.openmeetings.db.entity.file.Bas |
| `ImageConverter.convertDocument` | object | **SKIP** | never ran | 12,574 | 0 | 2/6 | 0/6 | 7/18 | 0/18 | - | never built on both sides — original: neither autofuzz nor constructor synthesis built java.util.Optional (NOT equivalence) |
| `RecordingConverter.startConversion` | object | **EQUIVALENT** | - | 4,322 | 4,322 | 1/10 | 1/2 | 10/38 | 8/22 | only 2/12 branches (17%) — weak | no divergence found in 15s of fuzzing |
| `LibraryChartLoader.loadChart` | object | **EQUIVALENT** | - | 5,967 | 5,967 | 0/0 | 0/0 | 7/10 | 6/8 | branchless (judge on Line) | no divergence found in 15s of fuzzing |
| `LdapOptions.ctor` | ctor | **DIVERGENT** | - | 2 | 2 | 2/8 | 0/0 | 53/63 | 23/23 | witnessed | **constructed state: useAdminForAttrs: false vs true** — on `[{}]` original returns org.apache.openmeetings.co, refactored returns org.apache.openmeetings.co |

## Divergences

- **`LdapOptions.ctor`** — **constructed state: useAdminForAttrs: false vs true** — on `[{}]` original returns org.apache.openmeetings.co, refactored returns org.apache.openmeetings.co

Reproduce with:
```bash
grep -a -A6 -F '[DIFFERENTIAL MISMATCH]' target/fuzz-logs/openmeetings/<Class>_<method>.log
```

## EQUIVALENT verdicts to distrust

These were never contradicted, but the fuzzer reached too little of the code for that to mean much. Raise `--duration`, or improve the seed corpus.

- `RecordingConverter.startConversion` — only 2/12 branches (17%) — weak

<!-- config: seeds=0 duration=15s mode=fuzz keepcorpus=False coverage=jazzer -->
