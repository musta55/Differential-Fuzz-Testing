@Override
public DenseBlock set(int[] ix, double v) {
    _data[pos(ix)] = (float) v;
    return this;
}