// Refactored to an arithmetic shift. Differs from n / 2 for negative odd n -> DIVERGENT.
public int half(Integer n) {
    return n >> 1;
}