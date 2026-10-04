@Override
protected ICLAScheme updateV(MatrixBlock data, IColIndex columns) {
    final int nRow = data.getNumRows();
    final int nColScheme = vals.length;
    for (int r = 0; r < nRow; r++) for (int c = 0; c < nColScheme; c++) {
        final double v = data.get(r, cols.get(c));
        if (!Util.eq(v, vals[c]))
            return updateToDDC(data, columns);
    }
    return this;
}