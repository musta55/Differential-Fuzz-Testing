private ICLAScheme updateDenseT(MatrixBlock data, IColIndex columns) {
    final DenseBlock db = data.getDenseBlock();
    for (int i = 0; i < columns.size(); i++) {
        final int col = columns.get(i);
        final double[] vals = db.values(col);
        final int nCol = data.getNumColumns();
        final int start = db.pos(col);
        for (int off = db.pos(col); i < nCol + start; off++) if (vals[off] != 0)
            return updateToHigherSchemeT(data, columns);
    }
    return this;
}