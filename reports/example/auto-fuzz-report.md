# Differential-Fuzzing Report — example

Generated 2026-09-25 19:01:17 · mode **fuzz** · **15s** per method · seed corpus **39** files · carried-over corpus **cleared**

- original tree: `/home/kgdesilva/FSE2027/Reu-refactoring-NEW-/reu-refactoring/modules/differential-fuzz-testing/Differential-Fuzz-Testing/examples/demo/original`
- refactored tree: `/home/kgdesilva/FSE2027/Reu-refactoring-NEW-/reu-refactoring/modules/differential-fuzz-testing/Differential-Fuzz-Testing/examples/demo/refactored`

## Summary

**3 classes · 8 changed methods**

| Outcome | Count | Meaning |
|---|---:|---|
| **EQUIVALENT** | 3 | no input made the two versions differ within the budget — trust it in proportion to the Confidence column |
| **DIVERGENT** | 3 | a concrete input makes them differ: a real behavioural change |
| **SKIP** | 2 | no verdict could be reached — see the breakdown below |

**6 of 8 methods got a verdict**; 2 could not be processed. Only EQUIVALENT and DIVERGENT are evidence about behaviour.

**Testing effort:** every method got a fixed budget of **15s**, in which the fuzzer generated **601,186 inputs** and completed **58,809 differential comparisons** (both versions invoked on the same input). The per-method counts are in the table below.

> `Inputs` is every input the fuzzer produced; `Compared` is only those that got as far as running BOTH versions. A large gap means most inputs died building a receiver or an argument, which is exactly when an EQUIVALENT should be doubted — and it is invisible in surefire's "Tests run", which counts JUnit invocations (each seed once, plus one call for the whole fuzzing session), not fuzz iterations.

### Why the SKIPs

| Reason | Count | What it means |
|---|---:|---|
| never ran | 2 | the harness completed but no input ever built a receiver **and** arguments for **both** sides, so nothing was ever compared. Usually a fact about the refactoring — e.g. the refactored constructor throws. This is **not** equivalence |

> Fuzzing is **sound for non-equivalence and incomplete for equivalence**: a DIVERGENT is a witnessed fact, an EQUIVALENT is only "no counterexample found in the budget". That is what the Confidence column is for — it reports how much of the two versions the fuzzer actually reached, so an EQUIVALENT at 0/8 branches can be told apart from one at 8/8.

## Per method

Coverage is **differential**: `orig` is the original class and `ref` the refactored one, each loaded in its own classloader and both measured by Jazzer in the same run.

| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | Branch ref | Line orig | Line ref | Confidence | Why |
|---|---|---|---|---:|---:|---|---|---|---|---|---|
| `Grader.grade` | scalar | **EQUIVALENT** | - | 21,076 | 21,076 | 6/6 | 6/6 | 7/7 | 7/7 | all 12 branches exercised | no divergence found in 15s of fuzzing |
| `Grader.passes` | scalar | **DIVERGENT** | - | 10 | 10 | 4/4 | 2/2 | 3/3 | 1/1 | witnessed | **exception type** — on `[-39]` original throws IllegalArgumentException, refactored returns false |
| `Grader.total` | object | **EQUIVALENT** | - | 19,146 | 19,146 | 3/4 | 3/4 | 6/6 | 6/7 | 6/8 branches (75%) | no divergence found in 15s of fuzzing |
| `Simple.ctor` | ctor | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 2/2 | 2/2 | witnessed | **exception type** — on `[]` original returns example.Simple@5dcb4f5f, refactored throws Error |
| `Simple.foo` | scalar | **SKIP** | never ran | 273,579 | 0 | 0/0 | 0/0 | 0/1 | 0/1 | - | never built on both sides — refactored: neither autofuzz nor constructor synthesis built example.Simple (NOT equivalence) |
| `Simple.bar` | scalar | **SKIP** | never ran | 268,798 | 0 | 0/0 | 0/0 | 0/1 | 0/1 | - | never built on both sides — refactored: neither autofuzz nor constructor synthesis built example.Simple (NOT equivalence) |
| `Widget.combine` | scalar | **EQUIVALENT** | - | 18,559 | 18,559 | 0/0 | 0/0 | 1/1 | 1/1 | branchless (judge on Line) | no divergence found in 15s of fuzzing |
| `Widget.half` | scalar | **DIVERGENT** | - | 15 | 15 | 0/0 | 0/0 | 1/1 | 1/1 | witnessed | **return value** — on `[-1]` original returns 0, refactored returns -1 |

## Divergences

- **`Grader.passes`** — **exception type** — on `[-39]` original throws IllegalArgumentException, refactored returns false
- **`Simple.ctor`** — **exception type** — on `[]` original returns example.Simple@5dcb4f5f, refactored throws Error
- **`Widget.half`** — **return value** — on `[-1]` original returns 0, refactored returns -1

Reproduce with:
```bash
grep -a -A6 -F '[DIFFERENTIAL MISMATCH]' target/fuzz-logs/example/<Class>_<method>.log
```

<!-- config: seeds=39 duration=15s mode=fuzz keepcorpus=False coverage=jazzer -->
