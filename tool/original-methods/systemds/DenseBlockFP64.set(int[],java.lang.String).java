@Override
public DenseBlock set(int[] ix, String v) {
    _data[pos(ix)] = Double.parseDouble(v);
    return this;
}