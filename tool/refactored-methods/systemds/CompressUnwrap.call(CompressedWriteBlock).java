@Override
public MatrixBlock call(CompressedWriteBlock compressedWriteBlock) throws Exception {
    final MatrixBlock g = compressedWriteBlock.get();
    if (isCompressedMatrixBlock(g)) {
        return createCompressedMatrixBlock(g);
    } else if (g.isEmpty()) {
        return createEmptyCompressedMatrixBlock(g);
    } else {
        return new MatrixBlock(g);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isCompressedMatrixBlock(MatrixBlock matrixBlock) {
    return matrixBlock instanceof CompressedMatrixBlock;
}

private MatrixBlock createCompressedMatrixBlock(MatrixBlock matrixBlock) {
    return new CompressedMatrixBlock((CompressedMatrixBlock) matrixBlock);
}

private MatrixBlock createEmptyCompressedMatrixBlock(MatrixBlock matrixBlock) {
    return CompressedMatrixBlockFactory.createConstant(matrixBlock.getNumRows(), matrixBlock.getNumColumns(), 0.0);
}

