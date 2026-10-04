@Override
public DenseBlock set(int r, int c, double v) {
    if (_data[r] == null) {
        _data[r] = new double[_odims[0]];
    }
    _data[r][c] = v;
    return this;
}