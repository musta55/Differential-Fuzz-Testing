private static void prepNonZerosForMatrixMult(MatrixBlock mb, boolean update) {
    if (!update) {
        return;
    }
    if (!mb.isInSparseFormat()) {
        mb.setNonZeros((long) mb.getNumRows() * mb.getNumColumns());
    } else {
        mb.recomputeNonZeros();
    }
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

