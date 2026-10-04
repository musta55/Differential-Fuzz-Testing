@Override
public DenseBlock set(int[] ix, String v) {
    _blocks[index(ix[0])].set(pos(ix), Boolean.parseBoolean(v));
    return this;
}