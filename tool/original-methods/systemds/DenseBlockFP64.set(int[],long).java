@Override
public DenseBlock set(int[] ix, long v) {
    _data[pos(ix)] = v;
    return this;
}