@Override
public DenseBlock set(int[] ix, double v) {
    _blocks[index(ix[0])][pos(ix)] = String.valueOf(v);
    return this;
}