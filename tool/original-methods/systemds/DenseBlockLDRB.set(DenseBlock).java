@Override
public DenseBlock set(DenseBlock db) {
    // ToDo: Optimize if dense block types match
    // ToDo: Performance
    // this implementation needs to be robust against rows in the input
    // stretching over multiple blocks in the output and vice versa
    boolean aligned = blockSize() * _odims[0] == db.blockSize() * db._odims[0];
    if (aligned) {
        for (int ri = 0; ri < _rlen; ri += blockSize()) {
            int bix = ri / blockSize();
            double[] other = db.valuesAt(bix);
            IntStream.range(0, blockSize(bix) * _odims[0]).forEach(i -> setInternal(bix, i, other[i]));
        }
    } else {
        long globalPos = 0;
        int bsize = blockSize() * _odims[0];
        for (int bix = 0; bix < db.numBlocks(); bix++) {
            double[] other = db.valuesAt(bix);
            int blen = db.blockSize(bix) * db._odims[0];
            int bix2 = (int) (globalPos / bsize);
            int off2 = (int) (globalPos % bsize);
            int blen2 = size(bix2);
            for (int i = 0; i < Math.min(blen, blen2 - off2); i++) setInternal(bix2, off2 + i, other[i]);
            if (blen2 - off2 < blen)
                for (int i = blen2 - off2; i < blen; i++) setInternal(bix2 + 1, i - (blen2 - off2), other[i]);
            globalPos += blen;
        }
    }
    return this;
}