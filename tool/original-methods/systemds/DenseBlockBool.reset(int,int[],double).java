@Override
public void reset(int rlen, int[] odims, double v) {
    boolean bv = v != 0;
    int len = rlen * odims[0];
    if (len > capacity()) {
        _data = new BitSet(len);
        if (bv)
            _data.set(0, len);
    } else {
        _data.set(0, _data.size(), bv);
    }
    _rlen = rlen;
    _odims = odims;
}