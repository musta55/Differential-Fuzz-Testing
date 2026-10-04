@Override
public DenseBlock set(int rl, int ru, int cl, int cu, double v) {
    if (cl == 0 && cu == _odims[0])
        fillBlock(rl * _odims[0], ru * _odims[0], v);
    else
        for (int i = rl, ix = rl * _odims[0]; i < ru; i++, ix += _odims[0]) fillBlock(ix + cl, ix + cu, v);
    return this;
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

