@Override
public long getLong(int[] ix) {
    return _blocks[ix[0]][pos(ix)];
}