# Differential-Fuzzing Report — example

Generated 2026-09-15 17:15:17 · mode **fuzz** · **15s** per method · seed corpus **29** files · carried-over corpus **cleared**

- original tree: `/home/mhasan02/2ndSem/Differential-Fuzz-Testing/examples/demo/original`
- refactored tree: `/home/mhasan02/2ndSem/Differential-Fuzz-Testing/examples/demo/refactored`

## Summary

**2 classes · 4 changed methods**

| Outcome | Count | Meaning |
|---|---:|---|
| **EQUIVALENT** | 2 | no input made the two versions differ within the budget — trust it in proportion to the Confidence column |
| **DIVERGENT** | 2 | a concrete input makes them differ: a real behavioural change |
| **SKIP** | 0 | no verdict could be reached — see the breakdown below |

**4 of 4 methods got a verdict**; 0 could not be processed. Only EQUIVALENT and DIVERGENT are evidence about behaviour.

**Testing effort:** every method got a fixed budget of **15s**, in which the fuzzer generated **47,714 inputs** and completed **47,714 differential comparisons** (both versions invoked on the same input). The per-method counts are in the table below.

> `Inputs` is every input the fuzzer produced; `Compared` is only those that got as far as running BOTH versions. A large gap means most inputs died building a receiver or an argument, which is exactly when an EQUIVALENT should be doubted — and it is invisible in surefire's "Tests run", which counts JUnit invocations (each seed once, plus one call for the whole fuzzing session), not fuzz iterations.

> Fuzzing is **sound for non-equivalence and incomplete for equivalence**: a DIVERGENT is a witnessed fact, an EQUIVALENT is only "no counterexample found in the budget". That is what the Confidence column is for — it reports how much of the two versions the fuzzer actually reached, so an EQUIVALENT at 0/8 branches can be told apart from one at 8/8.

## Per method

Coverage is **differential**: `orig` is the `<Class>Original` snapshot, `ref` the `<Class>Refactored` one, both measured by Jazzer in the same run.

| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | Branch ref | Line orig | Line ref | Confidence | Why |
|---|---|---|---|---:|---:|---|---|---|---|---|---|
| `Grader.grade` | scalar | **EQUIVALENT** | - | 24,375 | 24,375 | 6/6 | 6/6 | 7/7 | 7/7 | all 12 branches exercised | no divergence found in 15s of fuzzing |
| `Grader.passes` | scalar | **DIVERGENT** | - | 8 | 8 | 4/4 | 2/2 | 3/3 | 1/1 | witnessed | **exception type** — on `[-2212]` original throws IllegalArgumentException, refactored returns false |
| `Grader.total` | object | **EQUIVALENT** | - | 23,328 | 23,328 | 3/4 | 3/4 | 6/6 | 6/7 | 6/8 branches (75%) | no divergence found in 15s of fuzzing |
| `Simple.ctor` | ctor | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 2/2 | 2/2 | witnessed | **exception type** — on `[]` original returns example.SimpleOriginal@484, refactored throws Error |

## Divergences

- **`Grader.passes`** — **exception type** — on `[-2212]` original throws IllegalArgumentException, refactored returns false
- **`Simple.ctor`** — **exception type** — on `[]` original returns example.SimpleOriginal@484, refactored throws Error

Reproduce with:
```bash
grep -a -A6 -F '[DIFFERENTIAL MISMATCH]' target/fuzz-logs/example/<Class>_<method>.log
```

<!-- config: seeds=29 duration=15s mode=fuzz keepcorpus=False coverage=jazzer -->
