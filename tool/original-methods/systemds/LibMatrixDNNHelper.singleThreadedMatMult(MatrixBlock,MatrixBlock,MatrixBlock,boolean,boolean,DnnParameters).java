protected static void singleThreadedMatMult(MatrixBlock m1, MatrixBlock m2, MatrixBlock ret, boolean recomputeNNZM1, boolean recomputeNNZM2, DnnParameters params) {
    if (!params.enableNative || m1.sparse || m2.sparse) {
        prepNonZerosForMatrixMult(m1, recomputeNNZM1);
        prepNonZerosForMatrixMult(m2, recomputeNNZM2);
        LibMatrixMult.matrixMult(m1, m2, ret, true);
    } else {
        ret.sparse = false;
        if (ret.getDenseBlock() == null)
            ret.allocateDenseBlock();
        NativeHelper.dmmdd(m1.getDenseBlockValues(), m2.getDenseBlockValues(), ret.getDenseBlockValues(), m1.rlen, m1.clen, m2.clen, 1);
    }
    //no need to maintain nnz exactly, as consumed by other operations
    ret.setNonZeros((long) ret.rlen * ret.clen);
}