@Override
public long getLong(int[] ix) {
    return _blocks[index(ix[0])][pos(ix)];
}