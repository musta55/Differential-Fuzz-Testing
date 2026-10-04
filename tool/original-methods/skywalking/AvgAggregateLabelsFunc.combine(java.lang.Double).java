@Override
public void combine(final Double value) {
    if (value == null) {
        return;
    }
    if (sum == null) {
        sum = value;
    } else {
        sum = sum + value;
    }
    count++;
}