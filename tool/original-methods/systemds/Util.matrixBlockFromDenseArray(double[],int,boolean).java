public static MatrixBlock matrixBlockFromDenseArray(double[] values, int nCol, boolean check) {
    final int nRow = values.length / nCol;
    DenseBlock dictV = new DenseBlockFP64(new int[] { nRow, nCol }, values);
    MatrixBlock ret = new MatrixBlock(nRow, nCol, dictV);
    if (check) {
        ret.recomputeNonZeros();
        ret.examSparsity(true);
    } else
        ret.setNonZeros(-1);
    return ret;
}