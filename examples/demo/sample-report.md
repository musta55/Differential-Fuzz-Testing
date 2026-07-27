# Differential-Fuzzing Report — example

- Generated: 2026-07-24 19:24:23  ·  Mode: **fuzz**  ·  20s/method
- 5 auto-fuzzable methods (manifest-driven; original=<Class>Original, refactored=<Class>Refactored).
- Branch/Line = JaCoCo per-method coverage. DIVERGENT = exception-type or return-value mismatch (one-sided TIMEOUT is NOT counted).

| Method | Result | Tests (fail) | Branch | Line | Evidence |
|--------|--------|--------------|--------|------|----------|
| Simple.ctor | **DIVERGENT** | 2 (2) | 0/0 | 2/2 | orig: returns example.SimpleOriginal vs ref: throws Error |
| Simple.foo | **DIVERGENT** | 2 (2) | 0/0 | 0/1 | orig: returns example.SimpleOriginal vs ref: throws Error |
| Simple.bar | **DIVERGENT** | 2 (2) | 0/0 | 0/1 | orig: returns example.SimpleOriginal vs ref: throws Error |
| Widget.combine | **EQUIVALENT** | 2 (0) | 0/0 | 1/1 | no divergence in 20s |
| Widget.half | **DIVERGENT** | 2 (1) | 0/0 | 1/1 | orig: returns -81 vs ref: returns -82 |

## Summary

- EQUIVALENT **1** · DIVERGENT **4** · SKIP **0** · ERROR **0**  (5 methods)
- SKIP = instance method with no no-arg constructor (receiver can't be built generically).
