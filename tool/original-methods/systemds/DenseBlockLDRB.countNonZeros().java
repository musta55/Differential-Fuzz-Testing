@Override
public long countNonZeros() {
    long nnz = 0;
    for (int i = 0; i < numBlocks() - 1; i++) {
        nnz += computeNnz(i, 0, blockSize() * _odims[0]);
    }
    return nnz + computeNnz(numBlocks() - 1, 0, blockSize(numBlocks() - 1) * _odims[0]);
}