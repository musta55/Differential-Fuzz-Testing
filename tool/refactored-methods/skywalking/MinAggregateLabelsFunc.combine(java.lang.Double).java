@Override
public void combine(final Double value) {
    if (value != null && (min == null || value < min)) {
        min = value;
    }
}