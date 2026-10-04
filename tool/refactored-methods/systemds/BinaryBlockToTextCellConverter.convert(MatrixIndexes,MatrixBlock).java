/**
 * Before calling convert, please make sure to setBlockSize(blen, blen);
 */
@Override
public void convert(MatrixIndexes k1, MatrixBlock v1) {
    reset();
    startIndexes.setIndexes(UtilFunctions.computeCellIndex(k1.getRowIndex(), brow, 0), UtilFunctions.computeCellIndex(k1.getColumnIndex(), bcolumn, 0));
    sparse = v1.isInSparseFormat();
    thisBlockWidth = v1.getNumColumns();
    if (sparse) {
        sparseIterator = v1.getSparseBlockIterator();
    } else {
        if (v1.getDenseBlock() == null)
            return;
        denseArray = extractDenseArray(v1);
        nextInDenseArray = 0;
        denseArraySize = v1.getNumRows() * v1.getNumColumns();
    }
    hasValue = (v1.getNonZeros() > 0);
}
// ---- helper method(s) introduced by the refactoring ----
private double[] extractDenseArray(MatrixBlock v1) {
    if (v1.getDenseBlock() instanceof DenseBlockFP64DEDUP) {
        DenseBlockFP64DEDUP db = (DenseBlockFP64DEDUP) v1.getDenseBlock();
        double[] array = new double[v1.rlen * v1.clen];
        for (int i = 0; i < v1.rlen; i++) {
            double[] row = db.values(i);
            for (int j = 0; j < v1.clen; j++) {
                array[i * v1.clen + j] = row[j];
            }
        }
        return array;
    } else {
        return v1.getDenseBlockValues();
    }
}

