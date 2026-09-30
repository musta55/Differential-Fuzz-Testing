package e2e;

public abstract class Shape {

    protected final int scale;

    protected Shape(int scale) {
        this.scale = scale;
    }

    protected abstract int sides();

    public int area(int x) {
        return sides() + scale * x;
    }
}
