public static MatrixBlock getUniqueValues(MatrixBlock blkIn, Types.Direction dir) {
    //similar to R's unique, this operation takes a matrix and computes the
    //unique values (or rows in case of multiple column inputs)
    int rlen = blkIn.getNumRows();
    int clen = blkIn.getNumColumns();
    MatrixBlock blkOut = null;
    // TODO optimize for dense/sparse/compressed (once multi-column support added)
    switch(dir) {
        case RowCol:
            {
                HashSet<Double> hashSet = collectUniqueValues(blkIn, rlen, clen);
                blkOut = createOutputMatrixFromSet(hashSet, 1);
                break;
            }
        case Row:
            {
                int maxUniquePerRow = getMaxUniquePerRow(blkIn, rlen, clen);
                blkOut = createOutputMatrixFromRows(blkIn, rlen, clen, maxUniquePerRow);
                break;
            }
        case Col:
            {
                int maxUniquePerCol = getMaxUniquePerColumn(blkIn, rlen, clen);
                blkOut = createOutputMatrixFromColumns(blkIn, rlen, clen, maxUniquePerCol);
                break;
            }
        default:
            throw new IllegalArgumentException("Unrecognized direction: " + dir);
    }
    return blkOut;
}
// ---- helper method(s) introduced by the refactoring ----
private static HashSet<Double> collectUniqueValues(MatrixBlock blkIn, int rlen, int clen) {
    HashSet<Double> hashSet = new HashSet<>();
    for (int i = 0; i < rlen; i++) {
        for (int j = 0; j < clen; j++) hashSet.add(blkIn.get(i, j));
    }
    return hashSet;
}

private static MatrixBlock createOutputMatrixFromSet(HashSet<Double> hashSet, int cols) {
    int rlen2 = hashSet.size();
    MatrixBlock blkOut = new MatrixBlock(rlen2, cols, false).allocateBlock();
    int pos = 0;
    for (Double val : hashSet) blkOut.set(pos++, 0, val);
    return blkOut;
}

private static int getMaxUniquePerRow(MatrixBlock blkIn, int rlen, int clen) {
    HashSet<Double> hashSet = new HashSet<>();
    int maxUniquePerRow = 0;
    for (int i = 0; i < rlen; i++) {
        hashSet.clear();
        for (int j = 0; j < clen; j++) hashSet.add(blkIn.get(i, j));
        maxUniquePerRow = Math.max(maxUniquePerRow, hashSet.size());
    }
    return maxUniquePerRow;
}

private static MatrixBlock createOutputMatrixFromRows(MatrixBlock blkIn, int rlen, int clen, int maxUniquePerRow) {
    HashSet<Double> hashSet = new HashSet<>();
    MatrixBlock blkOut = new MatrixBlock(rlen, maxUniquePerRow, false).allocateBlock();
    for (int i = 0; i < rlen; i++) {
        hashSet.clear();
        for (int j = 0; j < clen; j++) hashSet.add(blkIn.get(i, j));
        int pos = 0;
        for (Double val : hashSet) blkOut.set(i, pos++, val);
    }
    return blkOut;
}

private static int getMaxUniquePerColumn(MatrixBlock blkIn, int rlen, int clen) {
    HashSet<Double> hashSet = new HashSet<>();
    int maxUniquePerCol = 0;
    for (int j = 0; j < clen; j++) {
        hashSet.clear();
        for (int i = 0; i < rlen; i++) hashSet.add(blkIn.get(i, j));
        maxUniquePerCol = Math.max(maxUniquePerCol, hashSet.size());
    }
    return maxUniquePerCol;
}

private static MatrixBlock createOutputMatrixFromColumns(MatrixBlock blkIn, int rlen, int clen, int maxUniquePerCol) {
    HashSet<Double> hashSet = new HashSet<>();
    MatrixBlock blkOut = new MatrixBlock(maxUniquePerCol, clen, false).allocateBlock();
    for (int j = 0; j < clen; j++) {
        hashSet.clear();
        for (int i = 0; i < rlen; i++) hashSet.add(blkIn.get(i, j));
        int pos = 0;
        for (Double val : hashSet) blkOut.set(pos++, j, val);
    }
    return blkOut;
}

