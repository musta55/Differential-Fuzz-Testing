@Override
public DenseBlock set(int[] ix, String v) {
    _data[pos(ix)] = Float.parseFloat(v);
    return this;
}