@Override
public DenseBlock set(int[] ix, String v) {
    _blocks[index(ix[0])][pos(ix)] = Double.parseDouble(v);
    return this;
}