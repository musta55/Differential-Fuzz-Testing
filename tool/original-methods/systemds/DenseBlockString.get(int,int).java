@Override
public double get(int r, int c) {
    String s = _data[pos(r, c)];
    return s == null || s.isEmpty() ? 0 : Double.parseDouble(s);
}