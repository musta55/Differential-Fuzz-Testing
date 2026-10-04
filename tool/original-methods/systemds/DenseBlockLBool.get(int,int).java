@Override
public double get(int r, int c) {
    return _blocks[index(r)].get(pos(r, c)) ? 1 : 0;
}