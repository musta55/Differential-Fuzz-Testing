@Override
public DenseBlock set(int r, double[] v) {
    int bix = index(r);
    int offset = pos(r);
    IntStream.range(0, _odims[0]).forEach((i) -> setInternal(bix, offset + i, v[i]));
    return this;
}