package e2e;

public class Broken {

    // Expected: REFACTORING_BROKEN in compile_status.csv, and no row in the fuzzer results.
    public static int value(int x) {
        return x + undefinedName;
    }
}
