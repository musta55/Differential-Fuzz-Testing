@Override
public void incr(int r, int c, double delta) {
    _blocks[index(r)][pos(r, c)] += delta;
}