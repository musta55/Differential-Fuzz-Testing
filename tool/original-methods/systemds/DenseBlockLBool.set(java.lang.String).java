@Override
public DenseBlock set(String s) {
    boolean b = Boolean.parseBoolean(s);
    for (int i = 0; i < numBlocks() - 1; i++) {
        _blocks[i].set(0, blockSize() * _odims[0], b);
    }
    _blocks[numBlocks() - 1].set(0, blockSize(numBlocks() - 1) * _odims[0], b);
    return this;
}