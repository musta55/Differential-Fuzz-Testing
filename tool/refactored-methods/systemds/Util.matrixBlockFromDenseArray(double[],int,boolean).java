public static MatrixBlock matrixBlockFromDenseArray(double[] values, int nCol, boolean check) {
    int nRow = values.length / nCol;
    DenseBlock dictV = new DenseBlockFP64(new int[] { nRow, nCol }, values);
    MatrixBlock ret = new MatrixBlock(nRow, nCol, dictV);
    if (check) {
        ret.recomputeNonZeros();
        ret.examSparsity(true);
    } else {
        ret.setNonZeros(-1);
    }
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private static int mergeSortedArrays(int[] lhs, int[] rhs, int[] joined) {
    int lp = 0, rp = 0, i = 0;
    while (lp < lhs.length && rp < rhs.length) {
        joined[i++] = lhs[lp] < rhs[rp] ? lhs[lp++] : rhs[rp++];
    }
    return i;
}

private static void appendRemainingElements(int[] array, int[] joined, int startIndex) {
    for (int i = startIndex, p = 0; p < array.length; i++, p++) {
        joined[i] = array[p];
    }
}

