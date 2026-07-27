package example;

// A class whose constructor is UNCHANGED, so its methods get a working receiver and are
// judged on their own merits — one truly-equivalent refactoring and one genuine divergence.
public class Widget {
    private final int base;

    public Widget(int base) {
        this.base = base;
    }

    // Reordered addition — same result for every input -> EQUIVALENT.
    public int combine(int a, int b) {
        return base + b + a;
    }

    // Refactored to an arithmetic shift. Differs from n / 2 for negative odd n -> DIVERGENT.
    public int half(Integer n) {
        return n >> 1;
    }
}

// Secondary top-level class, identical in both trees (exercises Fix C: it is relocated to
// its own Helper.java rather than duplicated into both snapshots).
class Helper {
    static int noop() {
        return 0;
    }
}
