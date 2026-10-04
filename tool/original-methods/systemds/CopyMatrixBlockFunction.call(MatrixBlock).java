@Override
public MatrixBlock call(MatrixBlock arg0) throws Exception {
    if (_deepCopy)
        return new MatrixBlock(arg0);
    else
        return arg0;
}