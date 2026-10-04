@Override
public DenseBlock set(int[] ix, double v) {
    _data[pos(ix)] = v;
    return this;
}