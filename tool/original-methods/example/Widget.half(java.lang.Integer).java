// Boxed-Integer param (exercises B1). Divergent vs the refactored version on negative
// odd inputs: n / 2 truncates toward zero, n >> 1 floors -> DIVERGENT by return value.
public int half(Integer n) {
    return n / 2;
}