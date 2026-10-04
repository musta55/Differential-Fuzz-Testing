/**
 * Sort (order) a compressed matrix in place of the {@code order} built-in, while keeping the result compressed.
 *
 * The compressed fast-path only supports the case the user can benefit from: a single column held in a single column
 * group, sorted ascending and returning the sorted values (not the index permutation). For everything else (multiple
 * columns, multiple column groups, descending order, index return, or a column-group encoding without a sort
 * implementation) this returns {@code null} so the caller can fall back to a decompressed reorg.
 *
 * @param mb the compressed matrix to sort
 * @param fn the sort specification carried by the reorg operator
 * @return the sorted compressed matrix, or {@code null} if the compressed fast-path does not apply
 */
public static MatrixBlock sort(CompressedMatrixBlock mb, SortIndex fn) {
    if (!isSingleColumnSortApplicable(mb, fn)) {
        return null;
    }
    AColGroup sorted = sortSingleColumn(mb);
    if (sorted == null) {
        return null;
    }
    return createSortedCompressedMatrixBlock(mb, sorted);
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

