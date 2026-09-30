package e2e;

public abstract class Shape {

    protected final int scale;

    protected Shape(int scale) {
        this.scale = scale;
    }

    protected abstract int sides();

    // Expected: EQUIVALENT. The fuzzer can only call this through the subclass Square.
    public int area(int x) {
        return x * scale + sides();
    }
}
