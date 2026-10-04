@Override
public String toString() {
    if (isEmpty()) {
        return "";
    }
    StringBuilder sb = new StringBuilder();
    int[] indexes = indexes();
    double[] values = values();
    int rowDigits = calculateRowDigits(indexes);
    appendValues(sb, indexes, values, rowDigits);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateRowDigits(int[] indexes) {
    return (int) Math.max(Math.ceil(Math.log10(indexes[indexes.length - 1])), 1);
}

private void appendValues(StringBuilder sb, int[] indexes, double[] values, int rowDigits) {
    for (int i = 0; i < values.length; i++) {
        appendValue(sb, indexes[i], values[i], rowDigits);
        if (i + 1 < values.length) {
            sb.append(" ");
        }
    }
}

private void appendValue(StringBuilder sb, int index, double value, int rowDigits) {
    if (value == (long) value) {
        sb.append(String.format("%" + rowDigits + "d:%d", index, (long) value));
    } else {
        sb.append(String.format("%" + rowDigits + "d:%s", index, Double.toString(value)));
    }
}

