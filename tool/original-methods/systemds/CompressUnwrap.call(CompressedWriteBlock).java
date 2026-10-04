@Override
public MatrixBlock call(CompressedWriteBlock arg0) throws Exception {
    final MatrixBlock g = arg0.get();
    if (g instanceof CompressedMatrixBlock)
        return new CompressedMatrixBlock((CompressedMatrixBlock) g);
    else if (g.isEmpty())
        return CompressedMatrixBlockFactory.createConstant(g.getNumRows(), g.getNumColumns(), 0.0);
    else
        return new MatrixBlock(g);
}