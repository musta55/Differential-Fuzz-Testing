@Override
public long getLong(int[] ix) {
    return Long.parseLong(_blocks[index(ix[0])][pos(ix)]);
}