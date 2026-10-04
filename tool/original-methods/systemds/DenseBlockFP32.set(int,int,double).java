@Override
public DenseBlock set(int r, int c, double v) {
    _data[pos(r, c)] = (float) v;
    return this;
}