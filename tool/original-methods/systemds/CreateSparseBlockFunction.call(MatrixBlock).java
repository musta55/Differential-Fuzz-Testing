@Override
public MatrixBlock call(MatrixBlock arg0) throws Exception {
    //convert given block to CSR representation if in sparse format
    //but allow shallow pass-through if already in CSR representation.
    if (arg0.isInSparseFormat() && !arg0.isEmptyBlock(false) && !(arg0 instanceof CompressedMatrixBlock))
        return new MatrixBlock(arg0, _stype, false);
    else
        //pass through dense
        return arg0;
}