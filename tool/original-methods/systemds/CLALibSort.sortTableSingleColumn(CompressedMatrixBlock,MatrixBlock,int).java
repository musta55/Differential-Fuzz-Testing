private static MatrixBlock sortTableSingleColumn(CompressedMatrixBlock mb, MatrixBlock result, int k) {
    final long lnnz = mb.getNonZeros();
    if (// unknown number of non-zeros, cannot size the table.
    lnnz < 0)
        return null;
    final AColGroup sorted = sortSingleColumn(mb);
    if (sorted == null)
        return null;
    final int nRows = mb.getNumRows();
    final int nnz = (int) lnnz;
    final int zeroCount = nRows - nnz;
    // decompress the already-sorted single column once (ascending, zeros contiguous).
    final List<AColGroup> rg = new ArrayList<>(1);
    rg.add(sorted);
    final MatrixBlock sortedCol = new CompressedMatrixBlock(nRows, 1, lnnz, false, rg).decompress(k);
    // build the value/weight table: one row per non-zero value (weight 1) plus a single
    // collapsed zero row (weight = number of zeros). The row order is irrelevant because the
    // table is sorted by the reorg below, exactly as MatrixBlock.sortOperations does.
    final MatrixBlock tdw = new MatrixBlock(1 + nnz, 2, false);
    tdw.allocateDenseBlock();
    int w = 0;
    for (int i = 0; i < nRows; i++) {
        final double v = sortedCol.get(i, 0);
        if (v != 0) {
            tdw.set(w, 0, v);
            tdw.set(w, 1, 1);
            w++;
        }
    }
    // collapsed zero row (weight 0 when the column is dense)
    tdw.set(w, 0, 0);
    tdw.set(w, 1, zeroCount);
    // Emit through the same reorg used by MatrixBlock.sortOperations so the produced table is
    // bit-for-bit identical to the uncompressed path, including its (intentionally unmaintained)
    // non-zero metadata. This keeps downstream quantile/median consumers and result comparisons
    // consistent regardless of whether the input was compressed.
    if (result == null)
        result = new MatrixBlock(1 + nnz, 2, false);
    else
        result.reset(1 + nnz, 2, false);
    final ReorgOperator rop = new ReorgOperator(new SortIndex(1, false, false), k);
    LibMatrixReorg.reorg(tdw, result, rop);
    return result;
}