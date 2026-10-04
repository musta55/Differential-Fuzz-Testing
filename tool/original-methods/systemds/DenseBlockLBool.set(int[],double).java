@Override
public DenseBlock set(int[] ix, double v) {
    _blocks[index(ix[0])].set(pos(ix), v != 0);
    return this;
}