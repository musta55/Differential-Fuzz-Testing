@Override
public DenseBlock set(int r, int c, double v) {
    _blocks[index(r)].set(pos(r, c), v != 0);
    return this;
}