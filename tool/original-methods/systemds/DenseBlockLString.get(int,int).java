@Override
public double get(int r, int c) {
    return Double.parseDouble(_blocks[index(r)][pos(r, c)]);
}