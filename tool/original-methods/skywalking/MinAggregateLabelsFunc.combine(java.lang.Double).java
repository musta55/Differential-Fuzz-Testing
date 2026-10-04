@Override
public void combine(final Double value) {
    if (value == null) {
        return;
    }
    if (min == null) {
        min = value;
    } else {
        if (value < min) {
            min = value;
        }
    }
}