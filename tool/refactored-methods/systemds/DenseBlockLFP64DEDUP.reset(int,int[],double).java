@Override
public void reset(int rlen, int[] odims, double v) {
    boolean needsReallocation = rlen > capacity() / _odims[0];
    if (needsReallocation) {
        allocateBlocks(rlen);
    }
    for (int i = 0; i < rlen; i++) {
        if (needsReallocation && v != 0.0) {
            allocateBlock(i, odims[0]);
            Arrays.fill(_data[i], 0, odims[0], v);
        } else if (v == 0.0) {
            _data[i] = null;
        } else {
            if (odims[0] > _odims[0] || _data[i] == null) {
                allocateBlock(i, odims[0]);
            }
            Arrays.fill(_data[i], 0, odims[0], v);
        }
    }
    _blen = 1;
    _rlen = rlen;
    _odims = odims;
}