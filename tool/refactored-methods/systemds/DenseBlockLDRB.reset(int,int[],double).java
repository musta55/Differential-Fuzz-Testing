@Override
public void reset(int rlen, int[] odims, double v) {
    long dataLength = (long) rlen * odims[0];
    int newBlockSize = Math.min(rlen, MAX_ALLOC / odims[0]);
    int numBlocks = UtilFunctions.toInt(Math.ceil((double) rlen / newBlockSize));
    if (canReuseBlocks(newBlockSize, dataLength)) {
        resetWithExistingBlocks(numBlocks, newBlockSize, dataLength, v);
    } else {
        resetWithNewBlocks(numBlocks, newBlockSize, rlen, odims, v);
    }
    _blen = newBlockSize;
    _rlen = rlen;
    _odims = odims;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean canReuseBlocks(int newBlockSize, long dataLength) {
    return _blen == newBlockSize && dataLength <= capacity();
}

private void resetWithExistingBlocks(int numBlocks, int newBlockSize, long dataLength, double v) {
    IntStream.range(0, numBlocks).forEach(bi -> fillBlock(bi, 0, calculateToIndex(bi, newBlockSize, dataLength), v));
}

private void resetWithNewBlocks(int numBlocks, int newBlockSize, int rlen, int[] odims, double v) {
    int lastBlockSize = calculateLastBlockSize(newBlockSize, rlen, odims);
    allocateBlocks(numBlocks);
    IntStream.range(0, numBlocks).forEach(i -> {
        int length = calculateBlockLength(i, newBlockSize, lastBlockSize, odims);
        allocateBlock(i, length);
        if (v != 0) {
            fillBlock(i, 0, length, v);
        }
    });
}

private int calculateToIndex(int bi, int newBlockSize, long dataLength) {
    return (int) Math.min(newBlockSize, dataLength - bi * newBlockSize) * _odims[0];
}

private int calculateLastBlockSize(int newBlockSize, int rlen, int[] odims) {
    return (newBlockSize == rlen ? newBlockSize : rlen % newBlockSize) * odims[0];
}

private int calculateBlockLength(int i, int newBlockSize, int lastBlockSize, int[] odims) {
    return (i == numBlocks() - 1 ? lastBlockSize : newBlockSize * odims[0]);
}

private void addPositionForInnerDimensions(int pos, int[] ix) {
    for (int i = 1; i < ix.length - 1; i++) {
        pos += ix[i] * _odims[i];
    }
}

private long computeNnzForLastBlock() {
    return computeNnz(numBlocks() - 1, 0, blockSize(numBlocks() - 1) * _odims[0]);
}

private boolean isAllColumns(int cl, int cu) {
    return cl == 0 && cu == _odims[0];
}

private int adjustReForLastBlock(int bi, int ru) {
    return bi == index(ru - 1) ? pos(ru - 1) + _odims[0] : blockSize() * _odims[0];
}

private long computeNnzForBlock(int bi, int rb, int re, boolean allColumns, int cl, int cu) {
    if (allColumns) {
        return computeNnz(bi, rb, re - rb);
    } else {
        return computeNnzForPartialColumns(bi, rb, re, cl, cu);
    }
}

private long computeNnzForPartialColumns(int bi, int rb, int re, int cl, int cu) {
    long nnz = 0;
    for (int ri = rb; ri < re; ri += _odims[0]) {
        nnz += computeNnz(bi, ri + cl, cu - cl);
    }
    return nnz;
}

private void setValueForAllBlocks(double v) {
    for (int i = 0; i < numBlocks() - 1; i++) {
        fillBlock(i, 0, blockSize() * _odims[0], v);
    }
    fillBlock(numBlocks() - 1, 0, blockSize(numBlocks() - 1) * _odims[0], v);
}

private void setValueForBlock(int bi, int rb, int re, boolean allColumns, int cl, int cu, double v) {
    if (allColumns) {
        fillBlock(bi, rb, re, v);
    } else {
        setValueForPartialColumns(bi, rb, re, cl, cu, v);
    }
}

private void setValueForPartialColumns(int bi, int rb, int re, int cl, int cu, double v) {
    for (int ri = rb; ri < re; ri += _odims[0]) {
        fillBlock(bi, ri + cl, ri + cu, v);
    }
}

private void setValuesInBlock(int bix, int offset, double[] v) {
    IntStream.range(0, _odims[0]).forEach(i -> setInternal(bix, offset + i, v[i]));
}

private boolean isAligned(DenseBlock db) {
    return blockSize() * _odims[0] == db.blockSize() * db._odims[0];
}

private void setValuesForAlignedBlocks(DenseBlock db) {
    for (int ri = 0; ri < _rlen; ri += blockSize()) {
        int bix = ri / blockSize();
        double[] other = db.valuesAt(bix);
        setValuesInBlock(bix, other);
    }
}

private void setValuesInBlock(int bix, double[] other) {
    IntStream.range(0, blockSize(bix) * _odims[0]).forEach(i -> setInternal(bix, i, other[i]));
}

private void setValuesForUnalignedBlocks(DenseBlock db) {
    long globalPos = 0;
    int bsize = blockSize() * _odims[0];
    for (int bix = 0; bix < db.numBlocks(); bix++) {
        double[] other = db.valuesAt(bix);
        int blen = db.blockSize(bix) * db._odims[0];
        int bix2 = (int) (globalPos / bsize);
        int off2 = (int) (globalPos % bsize);
        int blen2 = size(bix2);
        setValuesInCurrentBlock(bix2, off2, blen, blen2, other);
        setValuesInNextBlock(bix2, off2, blen, blen2, other);
        globalPos += blen;
    }
}

private void setValuesInCurrentBlock(int bix2, int off2, int blen, int blen2, double[] other) {
    for (int i = 0; i < Math.min(blen, blen2 - off2); i++) {
        setInternal(bix2, off2 + i, other[i]);
    }
}

private void setValuesInNextBlock(int bix2, int off2, int blen, int blen2, double[] other) {
    if (blen2 - off2 < blen) {
        for (int i = blen2 - off2; i < blen; i++) {
            setInternal(bix2 + 1, i - (blen2 - off2), other[i]);
        }
    }
}

