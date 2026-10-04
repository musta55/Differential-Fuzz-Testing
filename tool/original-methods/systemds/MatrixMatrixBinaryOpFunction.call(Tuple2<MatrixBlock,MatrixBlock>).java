@Override
public MatrixBlock call(Tuple2<MatrixBlock, MatrixBlock> arg0) throws Exception {
    MatrixBlock left = arg0._1();
    MatrixBlock right = arg0._2();
    if (right instanceof CompressedMatrixBlock)
        return ((CompressedMatrixBlock) right).binaryOperationsLeft(_bop, left, new MatrixBlock());
    else
        return left.binaryOperations(_bop, right, new MatrixBlock());
}