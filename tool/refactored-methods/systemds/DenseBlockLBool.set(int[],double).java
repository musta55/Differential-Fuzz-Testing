@Override
public DenseBlock set(int[] ix, double v) {
    setBlockValue(ix, v);
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetExistingBlocks(int numBlocks, boolean bv, int newBlockSize, long dataLength) {
    for (int i = 0; i < numBlocks; i++) {
        int toIndex = (int) Math.min(newBlockSize, dataLength - i * newBlockSize) * _odims[0];
        _blocks[i].set(0, toIndex, bv);
        _blocks[i].set(toIndex, _blocks[i].size(), false);
    }
}

private void resetNewBlocks(int numBlocks, boolean bv, int newBlockSize, long dataLength) {
    int lastBlockSize = (newBlockSize == _rlen ? newBlockSize : _rlen % newBlockSize) * _odims[0];
    allocateBlocks(numBlocks);
    IntStream.range(0, numBlocks).forEach((i) -> {
        int length = i == numBlocks - 1 ? lastBlockSize : newBlockSize;
        allocateBlock(i, length);
        _blocks[i].set(0, length, bv);
    });
}

private void setAllBlocks(boolean b) {
    for (int i = 0; i < numBlocks() - 1; i++) {
        _blocks[i].set(0, blockSize() * _odims[0], b);
    }
    _blocks[numBlocks() - 1].set(0, blockSize(numBlocks() - 1) * _odims[0], b);
}

private void setBlockValue(int r, int c, double v) {
    _blocks[index(r)].set(pos(r, c), v != 0);
}

private void setBlockValue(int[] ix, double v) {
    _blocks[index(ix[0])].set(pos(ix), v != 0);
}

private void setBlockValue(int[] ix, long v) {
    _blocks[index(ix[0])].set(pos(ix), v != 0);
}

private void setBlockValue(int[] ix, String v) {
    _blocks[index(ix[0])].set(pos(ix), Boolean.parseBoolean(v));
}

private double getBlockValue(int r, int c) {
    return _blocks[index(r)].get(pos(r, c)) ? 1 : 0;
}

private double getBlockValue(int[] ix) {
    return _blocks[index(ix[0])].get(pos(ix)) ? 1 : 0;
}

