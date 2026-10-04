public DenseBlockBool(int[] dims) {
    this(dims, 0);
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

