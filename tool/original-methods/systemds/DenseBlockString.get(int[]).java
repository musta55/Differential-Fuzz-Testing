@Override
public double get(int[] ix) {
    String s = _data[pos(ix)];
    return s == null || s.isEmpty() ? 0 : Double.parseDouble(s);
}