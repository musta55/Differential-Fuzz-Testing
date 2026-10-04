@Override
public DenseBlock set(int r, double[] v) {
    int row = pos(r);
    for (int i = 0; i < _odims[0]; i++) {
        _data[row + i] = (float) v[i];
    }
    return this;
}