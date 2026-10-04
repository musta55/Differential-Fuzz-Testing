@Override
public DenseBlock set(int[] ix, long v) {
    setValue(pos(ix), v);
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void allocateIfNecessary(int len) {
    if (len > capacity())
        _data = new float[len];
}

private void fillData(int len, double v) {
    if (v != 0)
        Arrays.fill(_data, 0, len, (float) v);
    else
        Arrays.fill(_data, 0, len, 0);
}

private void setValue(int index, double v) {
    _data[index] = (float) v;
}

private void setValue(int index, long v) {
    _data[index] = v;
}

private void setValue(int index, String v) {
    _data[index] = Float.parseFloat(v);
}

