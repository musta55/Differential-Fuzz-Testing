@Override
public String getString(int[] ix) {
    return String.valueOf(_blocks[index(ix[0])][pos(ix)]);
}