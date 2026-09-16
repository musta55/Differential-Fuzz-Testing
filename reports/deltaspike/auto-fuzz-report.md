# Differential-Fuzzing Report — deltaspike

Generated 2026-09-16 00:06:26 · mode **fuzz** · **30s** per method · seed corpus **4** files · carried-over corpus **cleared**

- original tree: `/home/mhasan02/2ndSem/RefAgent-reproduce/projects/before/deltaspike`
- refactored tree: `/home/mhasan02/2ndSem/RefAgent-reproduce/projects/after/deltaspike`

## Summary

**4 classes · 5 changed methods**

| Outcome | Count | Meaning |
|---|---:|---|
| **EQUIVALENT** | 5 | no input made the two versions differ within the budget — trust it in proportion to the Confidence column |
| **DIVERGENT** | 0 | a concrete input makes them differ: a real behavioural change |
| **SKIP** | 0 | no verdict could be reached — see the breakdown below |

**5 of 5 methods got a verdict**; 0 could not be processed. Only EQUIVALENT and DIVERGENT are evidence about behaviour.

**Testing effort:** every method got a fixed budget of **30s**, in which the fuzzer generated **207,449 inputs** and completed **205,019 differential comparisons** (both versions invoked on the same input). The per-method counts are in the table below.

> `Inputs` is every input the fuzzer produced; `Compared` is only those that got as far as running BOTH versions. A large gap means most inputs died building a receiver or an argument, which is exactly when an EQUIVALENT should be doubted — and it is invisible in surefire's "Tests run", which counts JUnit invocations (each seed once, plus one call for the whole fuzzing session), not fuzz iterations.

> Fuzzing is **sound for non-equivalence and incomplete for equivalence**: a DIVERGENT is a witnessed fact, an EQUIVALENT is only "no counterexample found in the budget". That is what the Confidence column is for — it reports how much of the two versions the fuzzer actually reached, so an EQUIVALENT at 0/8 branches can be told apart from one at 8/8.

## Per method

Coverage is **differential**: `orig` is the `<Class>Original` snapshot, `ref` the `<Class>Refactored` one, both measured by Jazzer in the same run.

| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | Branch ref | Line orig | Line ref | Confidence | Why |
|---|---|---|---|---:|---:|---|---|---|---|---|---|
| `Car.onPostConstruct` | scalar | **EQUIVALENT** | - | 40,656 | 40,656 | 0/0 | 0/0 | 3/3 | 2/2 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `CarRepair.onPostConstruct` | scalar | **EQUIVALENT** | - | 41,940 | 41,940 | 0/0 | 0/0 | 3/3 | 2/2 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `InjectableResourceLiteral.location` | scalar | **EQUIVALENT** | - | 38,513 | 37,413 | 0/0 | 0/0 | 1/1 | 1/1 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `InjectableResourceLiteral.resourceProvider` | scalar | **EQUIVALENT** | - | 39,235 | 37,905 | 0/0 | 0/0 | 1/1 | 1/1 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `NamedLiteral.ctor` | ctor | **EQUIVALENT** | - | 47,105 | 47,105 | 0/0 | 0/0 | 0/3 | 3/3 | branchless (judge on Line) | no divergence found in 30s of fuzzing |

<!-- config: seeds=4 duration=30s mode=fuzz keepcorpus=False coverage=jazzer -->
