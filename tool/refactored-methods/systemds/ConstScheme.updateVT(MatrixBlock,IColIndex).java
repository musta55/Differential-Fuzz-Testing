@Override
protected ICLAScheme updateVT(MatrixBlock data, IColIndex columns) {
    // TODO specialize for sparse data. But would only be used in rare cases
    final int nCol = data.getNumColumns();
    final int nColScheme = vals.length;
    for (int r = 0; r < nColScheme; r++) {
        final int row = cols.get(r);
        final double def = vals[r];
        if (!checkColumnForUpdate(data, row, nCol, def))
            return updateToDDCT(data, columns);
    }
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean checkRowForUpdate(MatrixBlock data, int row, int nColScheme) {
    for (int c = 0; c < nColScheme; c++) {
        final double v = data.get(row, cols.get(c));
        if (!Util.eq(v, vals[c]))
            return false;
    }
    return true;
}

private boolean checkColumnForUpdate(MatrixBlock data, int row, int nCol, double def) {
    for (int c = 0; c < nCol; c++) {
        final double v = data.get(row, c);
        if (!Util.eq(v, def))
            return false;
    }
    return true;
}

