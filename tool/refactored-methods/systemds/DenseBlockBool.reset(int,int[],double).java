@Override
public void reset(int rlen, int[] odims, double v) {
    boolean bv = v != 0;
    int len = rlen * odims[0];
    resetCommon(len, bv);
    _rlen = rlen;
    _odims = odims;
}
// ---- helper method(s) introduced by the refactoring ----
private DenseBlockBool(int[] dims, double v) {
    super(dims);
    reset(_rlen, _odims, v);
}

private void resetCommon(int len, boolean bv) {
    if (len > capacity()) {
        _data = new BitSet(len);
        if (bv)
            _data.set(0, len);
    } else {
        _data.set(0, _data.size(), bv);
    }
}

