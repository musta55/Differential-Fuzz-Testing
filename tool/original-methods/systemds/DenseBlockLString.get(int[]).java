@Override
public double get(int[] ix) {
    return Double.parseDouble(_blocks[index(ix[0])][pos(ix)]);
}