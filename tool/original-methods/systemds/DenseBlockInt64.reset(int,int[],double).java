@Override
public void reset(int rlen, int[] odims, double v) {
    int len = rlen * odims[0];
    if (len > capacity()) {
        _data = new long[len];
        if (v != 0)
            Arrays.fill(_data, (long) v);
    } else {
        Arrays.fill(_data, 0, len, (long) v);
    }
    _rlen = rlen;
    _odims = odims;
}