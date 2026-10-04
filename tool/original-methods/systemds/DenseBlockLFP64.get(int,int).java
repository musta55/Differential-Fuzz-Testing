@Override
public double get(int r, int c) {
    return _blocks[index(r)][pos(r, c)];
}