@Override
public long getLong(int[] ix) {
    return UtilFunctions.toLong(_blocks[index(ix[0])][pos(ix)]);
}