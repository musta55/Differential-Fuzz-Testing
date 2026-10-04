@Override
public DenseBlock set(double v) {
    for (int i = 0; i < numBlocks() - 1; i++) {
        fillBlock(i, 0, blockSize() * _odims[0], v);
    }
    fillBlock(numBlocks() - 1, 0, blockSize(numBlocks() - 1) * _odims[0], v);
    return this;
}