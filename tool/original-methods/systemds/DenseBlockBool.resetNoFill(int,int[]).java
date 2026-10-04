@Override
public void resetNoFill(int rlen, int[] odims) {
    int len = rlen * odims[0];
    if (len > capacity())
        _data = new BitSet(len);
    _rlen = rlen;
    _odims = odims;
}