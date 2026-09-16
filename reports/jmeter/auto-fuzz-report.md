# Differential-Fuzzing Report — jmeter

Generated 2026-09-15 16:56:26 · mode **fuzz** · **15s** per method · seed corpus **0** files · carried-over corpus **cleared**

- original tree: `/home/mhasan02/2ndSem/RefAgent-reproduce/projects/before/jmeter`
- refactored tree: `/home/mhasan02/2ndSem/RefAgent-reproduce/projects/after/jmeter`

## Summary

**4 classes · 5 changed methods**

| Outcome | Count | Meaning |
|---|---:|---|
| **EQUIVALENT** | 4 | no input made the two versions differ within the budget — trust it in proportion to the Confidence column |
| **DIVERGENT** | 0 | a concrete input makes them differ: a real behavioural change |
| **SKIP** | 1 | no verdict could be reached — see the breakdown below |

**4 of 5 methods got a verdict**; 1 could not be processed. Only EQUIVALENT and DIVERGENT are evidence about behaviour.

**Testing effort:** every method got a fixed budget of **15s**, in which the fuzzer generated **14,123 inputs** and completed **14,122 differential comparisons** (both versions invoked on the same input). The per-method counts are in the table below.

> `Inputs` is every input the fuzzer produced; `Compared` is only those that got as far as running BOTH versions. A large gap means most inputs died building a receiver or an argument, which is exactly when an EQUIVALENT should be doubted — and it is invisible in surefire's "Tests run", which counts JUnit invocations (each seed once, plus one call for the whole fuzzing session), not fuzz iterations.

### Why the SKIPs

| Reason | Count | What it means |
|---|---:|---|
| never ran | 1 | the harness completed but no input ever built a receiver **and** arguments for **both** sides, so nothing was ever compared. Usually a fact about the refactoring — e.g. the refactored constructor throws. This is **not** equivalence |

> Fuzzing is **sound for non-equivalence and incomplete for equivalence**: a DIVERGENT is a witnessed fact, an EQUIVALENT is only "no counterexample found in the budget". That is what the Confidence column is for — it reports how much of the two versions the fuzzer actually reached, so an EQUIVALENT at 0/8 branches can be told apart from one at 8/8.

## Per method

Coverage is **differential**: `orig` is the `<Class>Original` snapshot, `ref` the `<Class>Refactored` one, both measured by Jazzer in the same run.

| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | Branch ref | Line orig | Line ref | Confidence | Why |
|---|---|---|---|---:|---:|---|---|---|---|---|---|
| `BeanShellClient.main` | object | **SKIP** | never ran | 1 | 0 | 1/6 | 0/0 | 6/29 | 0/15 | - | never built on both sides — no [UNBUILT] diagnostic in the log (NOT equivalence) |
| `BSFAssertion.getResult` | object | **EQUIVALENT** | - | 4,315 | 4,315 | 2/2 | 2/2 | 11/15 | 10/12 | all 4 branches exercised | no divergence found in 15s of fuzzing |
| `BeanShellAssertion.getResult` | object | **EQUIVALENT** | - | 2,708 | 2,708 | 1/2 | 2/2 | 32/35 | 16/21 | 3/4 branches (75%) | no divergence found in 15s of fuzzing |
| `CompareAssertion.compareTime` | object | **EQUIVALENT** | - | 3,990 | 3,990 | 5/10 | 2/8 | 8/15 | 3/13 | only 7/18 branches (39%) — weak | no divergence found in 15s of fuzzing |
| `CompareAssertion.compareContent` | object | **EQUIVALENT** | - | 3,109 | 3,109 | 2/10 | 1/8 | 4/16 | 3/13 | only 3/18 branches (17%) — weak | no divergence found in 15s of fuzzing |

## EQUIVALENT verdicts to distrust

These were never contradicted, but the fuzzer reached too little of the code for that to mean much. Raise `--duration`, or improve the seed corpus.

- `CompareAssertion.compareTime` — only 7/18 branches (39%) — weak
- `CompareAssertion.compareContent` — only 3/18 branches (17%) — weak

<!-- config: seeds=0 duration=15s mode=fuzz keepcorpus=False coverage=jazzer -->
