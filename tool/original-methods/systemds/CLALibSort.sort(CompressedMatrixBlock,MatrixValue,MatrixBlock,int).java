/**
 * Compute the sorted value/weight table used by the quantile/median/IQM operations (the {@code sort} / qsort lop),
 * exploiting compression to sort the few distinct values instead of all rows.
 *
 * The compressed fast-path applies to an unweighted sort of a single column held in a single column group. The
 * produced table is bit-for-bit identical to {@link MatrixBlock#sortOperations(MatrixValue, MatrixBlock, int)}: a
 * {@code (1 + nnz) x 2} matrix holding one row per non-zero value (weight 1) plus a single collapsed row for the
 * zeros (weight = number of zeros), sorted ascending by value. For every other case (weights present, multiple
 * columns or groups, or an encoding without a sort implementation) it falls back to a decompressed sort.
 *
 * @param mb      the compressed matrix to sort
 * @param weights optional per-row weights, or {@code null}
 * @param result  the result matrix (reused by the fallback)
 * @param k       the parallelization degree
 * @return the sorted value/weight table
 */
public static MatrixBlock sort(CompressedMatrixBlock mb, MatrixValue weights, MatrixBlock result, int k) {
    final MatrixBlock w = CompressedMatrixBlock.getUncompressed(weights);
    if (w == null && mb.getNumColumns() == 1 && mb.getColGroups().size() == 1) {
        final MatrixBlock fast = sortTableSingleColumn(mb, result, k);
        if (fast != null)
            return fast;
    }
    // fallback to uncompressed sort.
    return CompressedMatrixBlock.getUncompressed(mb, "sortOperations", k).sortOperations(w, result, k);
}