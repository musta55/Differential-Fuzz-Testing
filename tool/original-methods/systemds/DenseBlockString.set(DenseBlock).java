@Override
public DenseBlock set(DenseBlock db) {
    int[] ix = new int[numDims()];
    for (int r = 0; r < _rlen; r++) {
        ix[0] = r;
        for (int c = 0; c < _odims[0]; c++) {
            // for linear scan
            ix[ix.length - 1] = c;
            _data[pos(r, c)] = db.getString(ix);
        }
    }
    return this;
}