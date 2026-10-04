@Override
public void combine(final Double value) {
    if (value != null && (max == null || value > max)) {
        max = value;
    }
}