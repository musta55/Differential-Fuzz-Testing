@Override
public Boolean call(MatrixBlock arg0) throws Exception {
    return !arg0.isEmptyBlock(false);
}