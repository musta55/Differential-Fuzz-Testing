@Override
public MatrixBlock call(Tuple2<MatrixBlock, MatrixBlock> arg0) throws Exception {
    MatrixBlock left = arg0._1();
    MatrixBlock right = arg0._2();
    return isRightCompressed(right) ? performCompressedOperation(left, right) : performStandardOperation(left, right);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isRightCompressed(MatrixBlock right) {
    return right instanceof CompressedMatrixBlock;
}

private MatrixBlock performCompressedOperation(MatrixBlock left, MatrixBlock right) {
    return ((CompressedMatrixBlock) right).binaryOperationsLeft(_bop, left, new MatrixBlock());
}

private MatrixBlock performStandardOperation(MatrixBlock left, MatrixBlock right) {
    return left.binaryOperations(_bop, right, new MatrixBlock());
}

