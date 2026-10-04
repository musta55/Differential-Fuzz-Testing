@Override
protected ICLAScheme updateV(MatrixBlock data, IColIndex columns) {
    final int nRow = data.getNumRows();
    final int nColScheme = vals.length;
    for (int r = 0; r < nRow; r++) if (!checkRowForUpdate(data, r, nColScheme))
        return updateToDDC(data, columns);
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

