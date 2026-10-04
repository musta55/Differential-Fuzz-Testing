@Override
public DenseBlock set(DenseBlock db) {
    int[] ix = new int[numDims()];
    int roff = 0;
    for (int bi = 0; bi < numBlocks(); bi++) {
        for (int r = 0; r < blockSize(); r++) {
            ix[0] = r + roff;
            for (int c = 0; c < _odims[0]; c++) {
                // for linear scan
                ix[ix.length - 1] = c;
                setStringValueFromDB(bi, r, c, db.getString(ix));
            }
        }
        roff += blockSize();
    }
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void fillAllBlocks(String s) {
    for (int i = 0; i < numBlocks() - 1; i++) {
        Arrays.fill(_blocks[i], 0, blockSize() * _odims[0], s);
    }
    Arrays.fill(_blocks[numBlocks() - 1], 0, blockSize(numBlocks() - 1) * _odims[0], s);
}

private void setStringValue(int r, int c, String value) {
    _blocks[index(r)][pos(r, c)] = value;
}

private void setStringValueFromDB(int bi, int r, int c, String value) {
    _blocks[bi][r * _odims[0] + c] = value;
}

private void setStringValueFromArray(int[] ix, String value) {
    _blocks[index(ix[0])][pos(ix)] = value;
}

private double parseDoubleValue(int r, int c) {
    return Double.parseDouble(_blocks[index(r)][pos(r, c)]);
}

private double parseDoubleValueFromArray(int[] ix) {
    return Double.parseDouble(_blocks[index(ix[0])][pos(ix)]);
}

private long parseLongValueFromArray(int[] ix) {
    return Long.parseLong(_blocks[index(ix[0])][pos(ix)]);
}

