package example;

// A class whose constructor is UNCHANGED, so its methods get a working receiver and are
// judged on their own merits — one truly-equivalent refactoring and one genuine divergence.
public class Widget {
    private final int base;

    public Widget(int base) {
        this.base = base;
    }

    // Body changed but semantically equivalent (integer add is commutative/associative,
    // overflow wraps identically) -> EQUIVALENT.
    public int combine(int a, int b) {
        return a + b + base;
    }

    // Boxed-Integer param (exercises B1). Divergent vs the refactored version on negative
    // odd inputs: n / 2 truncates toward zero, n >> 1 floors -> DIVERGENT by return value.
    public int half(Integer n) {
        return n / 2;
    }
}

// Secondary top-level class, identical in both trees (exercises Fix C: it is relocated to
// its own Helper.java rather than duplicated into both snapshots).
class Helper {
    static int noop() {
        return 0;
    }
}
