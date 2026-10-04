@Override
public DeltaDictionary applyScalarOp(ScalarOperator op) {
    if (op.fn instanceof Multiply || op.fn instanceof Divide) {
        final double[] retV = new double[_values.length];
        applyOperationToValues(retV, op);
        return new DeltaDictionary(retV, _numCols);
    } else {
        throw new NotImplementedException("Scalar op " + op.fn.getClass().getSimpleName() + " not supported in DeltaDictionary");
    }
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

