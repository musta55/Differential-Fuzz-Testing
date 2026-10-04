@Override
public MatrixBlock call(MatrixBlock matrixBlock) throws Exception {
    return deepCopy ? new MatrixBlock(matrixBlock) : matrixBlock;
}