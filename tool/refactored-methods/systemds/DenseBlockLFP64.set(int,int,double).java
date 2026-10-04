@Override
public DenseBlock set(int r, int c, double v) {
    setValue(r, c, v);
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void updateValue(int r, int c, double delta) {
    _blocks[index(r)][pos(r, c)] += delta;
}

private void setValue(int r, int c, double v) {
    _blocks[index(r)][pos(r, c)] = v;
}

private void setValue(int[] ix, double v) {
    _blocks[index(ix[0])][pos(ix)] = v;
}

private void setValue(int[] ix, long v) {
    _blocks[index(ix[0])][pos(ix)] = v;
}

private void setValue(int[] ix, String v) {
    _blocks[index(ix[0])][pos(ix)] = Double.parseDouble(v);
}

private double getValue(int r, int c) {
    return _blocks[index(r)][pos(r, c)];
}

private double getValue(int[] ix) {
    return _blocks[index(ix[0])][pos(ix)];
}

private void copyValuesFrom(DenseBlock db) {
    long globalPos = 0;
    int bsize = blockSize() * _odims[0];
    for (int bix = 0; bix < db.numBlocks(); bix++) {
        double[] other = db.valuesAt(bix);
        int blen = db.blockSize(bix) * db._odims[0];
        int bix2 = (int) (globalPos / bsize);
        int off2 = (int) (globalPos % bsize);
        int blen2 = size(bix2);
        System.arraycopy(other, 0, valuesAt(bix2), off2, Math.min(blen, blen2 - off2));
        if (blen2 - off2 < blen)
            System.arraycopy(other, blen2 - off2, valuesAt(bix2 + 1), 0, blen - (blen2 - off2));
        globalPos += blen;
    }
}

