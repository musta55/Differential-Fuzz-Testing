@Override
public double get(int[] ix) {
    return _blocks[index(ix[0])][pos(ix)];
}