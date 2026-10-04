@Override
public DenseBlock set(String s) {
    for (int i = 0; i < numBlocks() - 1; i++) {
        Arrays.fill(_blocks[i], 0, blockSize() * _odims[0], s);
    }
    Arrays.fill(_blocks[numBlocks() - 1], 0, blockSize(numBlocks() - 1) * _odims[0], s);
    return this;
}