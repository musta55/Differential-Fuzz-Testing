@Override
public Pair<NullWritable, Text> next() {
    if (!hasValue)
        return null;
    long i, j;
    double v;
    if (sparse) {
        IJV cell = sparseIterator.next();
        i = cell.getI() + startIndexes.getRowIndex();
        j = cell.getJ() + startIndexes.getColumnIndex();
        v = cell.getV();
    } else {
        i = startIndexes.getRowIndex() + nextInDenseArray / thisBlockWidth;
        j = startIndexes.getColumnIndex() + nextInDenseArray % thisBlockWidth;
        v = denseArray[nextInDenseArray];
        nextInDenseArray++;
    }
    value.set(i + " " + j + " " + v);
    return pair;
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

