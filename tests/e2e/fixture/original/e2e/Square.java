package e2e;

// Unchanged in both trees: never fuzzed itself, but needed to build a Shape.
public class Square extends Shape {

    public Square(int scale) {
        super(scale);
    }

    @Override
    protected int sides() {
        return 4;
    }
}
