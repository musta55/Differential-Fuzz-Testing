@Override
public DenseBlock set(int[] ix, String v) {
    setInternal(0, pos(ix), Double.parseDouble(v));
    return this;
}