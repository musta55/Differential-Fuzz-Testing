@Override
public DenseBlock set(int r, int c, double v) {
    _blocks[index(r)][pos(r, c)] = String.valueOf(v);
    return this;
}