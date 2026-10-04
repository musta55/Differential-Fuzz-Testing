@Override
public void reset(int rlen, int[] odims, double v) {
    int len = rlen * odims[0];
    if (len > capacity()) {
        allocateBlock(len);
        if (v != 0)
            fillBlock(0, len, v);
    } else {
        fillBlock(0, len, v);
    }
    _rlen = rlen;
    _odims = odims;
}
// ---- helper method(s) introduced by the refactoring ----
private void allocateBlock(int len) {
    _data = new double[len];
}

private void fillBlock(int start, int end, double v) {
    for (int i = start; i < end; i++) {
        _data[i] = v;
    }
}

private long computeNnz(int start, int length) {
    long nnz = 0;
    for (int i = start; i < start + length; i++) {
        if (_data[i] != 0) {
            nnz++;
        }
    }
    return nnz;
}

