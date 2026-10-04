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
    if (isUnweightedSingleColumnSortApplicable(mb, weights)) {
        MatrixBlock fast = sortTableSingleColumn(mb, result, k);
        if (fast != null) {
            return fast;
        }
    }
    // fallback to uncompressed sort.
    return CompressedMatrixBlock.getUncompressed(mb, "sortOperations", k).sortOperations(weights, result, k);
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isSingleColumnSortApplicable(CompressedMatrixBlock mb, SortIndex fn) {
    return mb.getNumColumns() == 1 && mb.getColGroups().size() == 1 && !fn.getDecreasing() && !fn.getIndexReturn();
}

private static boolean isUnweightedSingleColumnSortApplicable(CompressedMatrixBlock mb, MatrixValue weights) {
    return CompressedMatrixBlock.getUncompressed(weights) == null && mb.getNumColumns() == 1 && mb.getColGroups().size() == 1;
}

private static MatrixBlock createSortedCompressedMatrixBlock(CompressedMatrixBlock mb, AColGroup sorted) {
    List<AColGroup> rg = new ArrayList<>(1);
    rg.add(sorted);
    return new CompressedMatrixBlock(mb.getNumRows(), mb.getNumColumns(), mb.getNonZeros(), false, rg);
}

private static MatrixBlock createDecompressedSortedColumn(CompressedMatrixBlock mb, AColGroup sorted, int k) {
    List<AColGroup> rg = new ArrayList<>(1);
    rg.add(sorted);
    return new CompressedMatrixBlock(mb.getNumRows(), 1, mb.getNonZeros(), false, rg).decompress(k);
}

private static MatrixBlock createValueWeightTable(MatrixBlock sortedCol, int nRows, int nnz, int zeroCount) {
    MatrixBlock tdw = new MatrixBlock(1 + nnz, 2, false);
    tdw.allocateDenseBlock();
    int w = 0;
    for (int i = 0; i < nRows; i++) {
        double v = sortedCol.get(i, 0);
        if (v != 0) {
            tdw.set(w, 0, v);
            tdw.set(w, 1, 1);
            w++;
        }
    }
    // collapsed zero row (weight 0 when the column is dense)
    tdw.set(w, 0, 0);
    tdw.set(w, 1, zeroCount);
    return tdw;
}

private static MatrixBlock sortAndPrepareResult(MatrixBlock tdw, MatrixBlock result, int k) {
    if (result == null) {
        result = new MatrixBlock(1 + tdw.getNumRows(), tdw.getNumColumns(), false);
    } else {
        result.reset(1 + tdw.getNumRows(), tdw.getNumColumns(), false);
    }
    ReorgOperator rop = new ReorgOperator(new SortIndex(1, false, false), k);
    LibMatrixReorg.reorg(tdw, result, rop);
    return result;
}

