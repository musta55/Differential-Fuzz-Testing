private static MatrixBlock sortTableSingleColumn(CompressedMatrixBlock mb, MatrixBlock result, int k) {
    long lnnz = mb.getNonZeros();
    if (lnnz < 0) {
        // unknown number of non-zeros, cannot size the table.
        return null;
    }
    AColGroup sorted = sortSingleColumn(mb);
    if (sorted == null) {
        return null;
    }
    int nRows = mb.getNumRows();
    int nnz = (int) lnnz;
    int zeroCount = nRows - nnz;
    MatrixBlock sortedCol = createDecompressedSortedColumn(mb, sorted, k);
    MatrixBlock tdw = createValueWeightTable(sortedCol, nRows, nnz, zeroCount);
    return sortAndPrepareResult(tdw, result, k);
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

