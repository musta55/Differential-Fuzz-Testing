@Override
public DenseBlock set(int[] ix, long v) {
    setInternal(0, pos(ix), v);
    return this;
}