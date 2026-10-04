@Override
public long countNonZeros(int rl, int ru, int ol, int ou) {
    long nnz = 0;
    if (ol == 0 && ou == _odims[0]) {
        //specific case: all cols
        nnz += computeNnz(rl * _odims[0], (ru - rl) * _odims[0]);
    } else {
        for (int i = rl, ix = rl * _odims[0]; i < ru; i++, ix += _odims[0]) nnz += computeNnz(ix + ol, ou - ol);
    }
    return nnz;
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

