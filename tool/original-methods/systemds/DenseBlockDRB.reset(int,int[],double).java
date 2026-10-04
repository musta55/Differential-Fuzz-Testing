@Override
public void reset(int rlen, int[] odims, double v) {
    int len = rlen * odims[0];
    if (len > capacity()) {
        allocateBlock(0, len);
        if (v != 0)
            fillBlock(0, 0, len, v);
    } else {
        fillBlock(0, 0, len, v);
    }
    _rlen = rlen;
    _odims = odims;
}