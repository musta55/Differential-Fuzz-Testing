@Override
public DenseBlock set(int[] ix, double v) {
    setInternal(0, pos(ix), v);
    return this;
}