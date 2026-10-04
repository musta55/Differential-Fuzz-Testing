@Override
public void reset(int rlen, int[] odims, double v) {
    // Special implementation to make computeNnz fast if complete block is read
    boolean bv = v != 0;
    long dataLength = (long) rlen * odims[0];
    int newBlockSize = Math.min(rlen, MAX_ALLOC / odims[0]);
    int numBlocks = UtilFunctions.toInt(Math.ceil((double) rlen / newBlockSize));
    if (_blen == newBlockSize && dataLength <= capacity()) {
        for (int i = 0; i < numBlocks; i++) {
            int toIndex = (int) Math.min(newBlockSize, dataLength - i * newBlockSize) * _odims[0];
            _blocks[i].set(0, toIndex, bv);
            // Clear old data so we can use cardinality for computeNnz
            _blocks[i].set(toIndex, _blocks[i].size(), false);
        }
    } else {
        int lastBlockSize = (newBlockSize == rlen ? newBlockSize : rlen % newBlockSize) * odims[0];
        allocateBlocks(numBlocks);
        IntStream.range(0, numBlocks).forEach((i) -> {
            int length = i == numBlocks - 1 ? lastBlockSize : newBlockSize;
            allocateBlock(i, length);
            _blocks[i].set(0, length, bv);
        });
    }
    _blen = newBlockSize;
    _rlen = rlen;
    _odims = odims;
}