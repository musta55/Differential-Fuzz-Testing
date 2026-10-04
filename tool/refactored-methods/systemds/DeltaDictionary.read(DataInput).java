public static DeltaDictionary read(DataInput in) throws IOException {
    int numCols = in.readInt();
    int numValues = in.readInt();
    double[] values = new double[numValues];
    readValues(in, values);
    return new DeltaDictionary(values, numCols);
}
// ---- helper method(s) introduced by the refactoring ----
private void applyOperationToValues(double[] retV, ScalarOperator op) {
    for (int i = 0; i < _values.length; i++) retV[i] = op.executeScalar(_values[i]);
}

private void writeValues(DataOutput out) throws IOException {
    for (int i = 0; i < _values.length; i++) out.writeDouble(_values[i]);
}

private static void readValues(DataInput in, double[] values) throws IOException {
    for (int i = 0; i < values.length; i++) values[i] = in.readDouble();
}

private void appendValuesToStringBuilder(StringBuilder sb, int colIndexes) {
    for (int i = 0; i < _values.length; i++) {
        sb.append(_values[i]);
        if (i != _values.length - 1) {
            sb.append((i + 1) % colIndexes == 0 ? "\n" : ", ");
        }
    }
}

