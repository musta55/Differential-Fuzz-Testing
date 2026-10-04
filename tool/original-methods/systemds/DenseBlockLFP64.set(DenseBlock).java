@Override
public DenseBlock set(DenseBlock db) {
    // this implementation needs to be robust against rows in the input
    // stretching over multiple blocks in the output and vice versa
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
    return this;
}