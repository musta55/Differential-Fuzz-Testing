@Override
protected ICLAScheme updateVT(MatrixBlock data, IColIndex columns) {
    // TODO specialize for sparse data. But would only be used in rare cases
    final int nCol = data.getNumColumns();
    final int nColScheme = vals.length;
    for (int r = 0; r < nColScheme; r++) {
        final int row = cols.get(r);
        final double def = vals[r];
        for (int c = 0; c < nCol; c++) {
            final double v = data.get(row, c);
            if (!Util.eq(v, def))
                return updateToDDCT(data, columns);
        }
    }
    return this;
}