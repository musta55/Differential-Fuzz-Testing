package e2e;

public class Calc {

    // Expected: EQUIVALENT (addition is commutative, overflow wraps the same way).
    public static int add(int a, int b) {
        return a + b;
    }

    // Expected: DIVERGENT (n / 2 rounds toward zero, n >> 1 rounds down: -3 gives -1 vs -2).
    public static int half(int n) {
        return n / 2;
    }
}
