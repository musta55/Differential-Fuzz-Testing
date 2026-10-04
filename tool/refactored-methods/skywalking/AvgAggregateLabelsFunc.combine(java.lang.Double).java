@Override
public void combine(final Double value) {
    if (value == null) {
        return;
    }
    sum = sum == null ? value : sum + value;
    count++;
}