private ICLAScheme updateDenseT(MatrixBlock data, IColIndex columns) {
    final DenseBlock db = data.getDenseBlock();
    for (int i = 0; i < columns.size(); i++) {
        final int col = columns.get(i);
        final double[] vals = db.values(col);
        final int nCol = data.getNumColumns();
        final int start = db.pos(col);
        for (int off = start; off < start + nCol; off++) if (vals[off] != 0)
            return updateToHigherSchemeT(data, columns);
    }
    return this;
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

