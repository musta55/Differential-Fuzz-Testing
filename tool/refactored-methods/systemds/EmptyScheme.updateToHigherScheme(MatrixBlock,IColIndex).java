private ICLAScheme updateToHigherScheme(MatrixBlock data, IColIndex columns) {
    double[] vals = extractColumnValues(data, columns);
    return ConstScheme.create(columns, vals).update(data, columns);
}
// ---- helper method(s) introduced by the refactoring ----
private double[] extractColumnValues(MatrixBlock data, IColIndex columns) {
    double[] vals = new double[columns.size()];
    for (int c = 0; c < columns.size(); c++) vals[c] = data.get(0, c);
    return vals;
}

private double[] extractColumnValuesTransposed(MatrixBlock data, IColIndex columns) {
    double[] vals = new double[columns.size()];
    for (int c = 0; c < columns.size(); c++) vals[c] = data.get(c, 0);
    return vals;
}

