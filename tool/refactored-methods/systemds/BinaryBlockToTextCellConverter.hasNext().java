@Override
public boolean hasNext() {
    if (sparse) {
        hasValue = sparseIterator != null && sparseIterator.hasNext();
    } else {
        while (nextInDenseArray < denseArraySize && denseArray[nextInDenseArray] == 0) nextInDenseArray++;
        hasValue = nextInDenseArray < denseArraySize;
    }
    return hasValue;
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

