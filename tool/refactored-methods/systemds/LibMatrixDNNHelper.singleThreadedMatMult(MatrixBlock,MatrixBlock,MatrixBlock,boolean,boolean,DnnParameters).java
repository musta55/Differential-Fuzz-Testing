protected static void singleThreadedMatMult(MatrixBlock m1, MatrixBlock m2, MatrixBlock ret, boolean recomputeNNZM1, boolean recomputeNNZM2, DnnParameters params) {
    if (!params.enableNative || m1.sparse || m2.sparse) {
        handleSparseMatrixMultiplication(m1, recomputeNNZM1, m2, recomputeNNZM2, ret);
    } else {
        handleDenseMatrixMultiplication(m1, m2, ret);
    }
    ret.setNonZeros((long) ret.rlen * ret.clen);
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleSparseMatrixMultiplication(MatrixBlock m1, boolean recomputeNNZM1, MatrixBlock m2, boolean recomputeNNZM2, MatrixBlock ret) {
    prepNonZerosForMatrixMult(m1, recomputeNNZM1);
    prepNonZerosForMatrixMult(m2, recomputeNNZM2);
    LibMatrixMult.matrixMult(m1, m2, ret, true);
}

private static void handleDenseMatrixMultiplication(MatrixBlock m1, MatrixBlock m2, MatrixBlock ret) {
    ret.sparse = false;
    if (ret.getDenseBlock() == null) {
        ret.allocateDenseBlock();
    }
    NativeHelper.dmmdd(m1.getDenseBlockValues(), m2.getDenseBlockValues(), ret.getDenseBlockValues(), m1.rlen, m1.clen, m2.clen, 1);
}

