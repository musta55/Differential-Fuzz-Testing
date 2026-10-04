@Override
public MatrixBlock call(MatrixBlock mb) throws Exception {
    if (mb instanceof CompressedMatrixBlock) {
        CompressedMatrixBlock cmb = (CompressedMatrixBlock) mb;
        cmb.clearSoftReferenceToDecompressed();
        return cmb;
    }
    return mb;
}