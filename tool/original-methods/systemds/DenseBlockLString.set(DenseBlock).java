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
                _blocks[bi][r * _odims[0] + c] = db.getString(ix);
            }
        }
        roff += blockSize();
    }
    return this;
}