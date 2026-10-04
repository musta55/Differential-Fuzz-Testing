@Override
public MatrixBlock call(MatrixBlock mb) throws Exception {
    return clearDecompressedReferenceIfCompressed(mb);
}
// ---- helper method(s) introduced by the refactoring ----
private MatrixBlock clearDecompressedReferenceIfCompressed(MatrixBlock mb) {
    if (mb instanceof CompressedMatrixBlock) {
        CompressedMatrixBlock cmb = (CompressedMatrixBlock) mb;
        cmb.clearSoftReferenceToDecompressed();
        return cmb;
    }
    return mb;
}

