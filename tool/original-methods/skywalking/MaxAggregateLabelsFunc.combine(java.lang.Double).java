@Override
public void combine(final Double value) {
    if (value == null) {
        return;
    }
    if (max == null) {
        max = value;
    } else {
        if (value > max) {
            max = value;
        }
    }
}