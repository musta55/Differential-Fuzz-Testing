@Override
public void reset(int rlen, int[] odims, double v) {
    if (rlen > capacity() / _odims[0]) {
        this.allocateBlocks(rlen);
        if (v != 0.0) {
            for (int i = 0; i < rlen; i++) {
                allocateBlock(i, odims[0]);
                Arrays.fill(_data[i], 0, odims[0], v);
            }
        }
    } else {
        if (v == 0.0) {
            for (int i = 0; i < rlen; i++) _data[i] = null;
        } else {
            for (int i = 0; i < rlen; i++) {
                if (odims[0] > _odims[0] || _data[i] == null)
                    allocateBlock(i, odims[0]);
                Arrays.fill(_data[i], 0, odims[0], v);
            }
        }
    }
    _blen = 1;
    _rlen = rlen;
    _odims = odims;
}