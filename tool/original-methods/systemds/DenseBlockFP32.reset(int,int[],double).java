@Override
public void reset(int rlen, int[] odims, double v) {
    int len = rlen * odims[0];
    if (len > capacity()) {
        _data = new float[len];
        if (v != 0)
            Arrays.fill(_data, (float) v);
    } else {
        Arrays.fill(_data, 0, len, (float) v);
    }
    _rlen = rlen;
    _odims = odims;
}