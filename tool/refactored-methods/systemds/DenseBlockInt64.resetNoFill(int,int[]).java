@Override
public void resetNoFill(int rlen, int[] odims) {
    int len = rlen * odims[0];
    allocateIfNecessary(len);
    _rlen = rlen;
    _odims = odims;
}
// ---- helper method(s) introduced by the refactoring ----
private void allocateIfNecessary(int len) {
    if (len > capacity())
        _data = new long[len];
}

private void fillData(int len, double v) {
    long value = UtilFunctions.toLong(v);
    if (value != 0)
        Arrays.fill(_data, 0, len, value);
    else
        Arrays.fill(_data, 0, len, 0);
}

