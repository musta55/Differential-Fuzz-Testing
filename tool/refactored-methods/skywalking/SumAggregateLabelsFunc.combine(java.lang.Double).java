@Override
public void combine(final Double value) {
    if (value != null) {
        sum = (sum == null) ? value : sum + value;
    }
}