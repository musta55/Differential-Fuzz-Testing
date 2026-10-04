@Override
public Boolean call(MatrixBlock matrixBlock) throws Exception {
    return !matrixBlock.isEmptyBlock(false);
}