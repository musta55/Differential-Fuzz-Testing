@Override
public void reset(int rlen, int[] odims, double v) {
    long dataLength = (long) rlen * odims[0];
    int newBlockSize = Math.min(rlen, MAX_ALLOC / odims[0]);
    int numBlocks = UtilFunctions.toInt(Math.ceil((double) rlen / newBlockSize));
    if (_blen == newBlockSize && dataLength <= capacity()) {
        IntStream.range(0, numBlocks).forEach((bi) -> {
            int toIndex = (int) Math.min(newBlockSize, dataLength - bi * newBlockSize) * _odims[0];
            fillBlock(bi, 0, toIndex, v);
        });
    } else {
        int lastBlockSize = (newBlockSize == rlen ? newBlockSize : rlen % newBlockSize) * odims[0];
        allocateBlocks(numBlocks);
        IntStream.range(0, numBlocks).forEach(i -> {
            int length = (i == numBlocks - 1 ? lastBlockSize : newBlockSize * _odims[0]);
            allocateBlock(i, length);
            if (v != 0)
                fillBlock(i, 0, length, v);
        });
    }
    _blen = newBlockSize;
    _rlen = rlen;
    _odims = odims;
}