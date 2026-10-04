public static long estimateInMemorySizeLinearFunctional(int nrColumns, boolean contiguousColumns) {
    long size = estimateInMemorySizeGroup(nrColumns, contiguousColumns);
    // coefficients; per column, we store 2 doubles (slope & intercept)
    size += MemoryEstimates.doubleArrayCost(2L * nrColumns);
    // _numRows
    size += 4;
    return size;
}
// ---- helper method(s) introduced by the refactoring ----
private static int calculateAdditionalOffsetLength(int nrRows) {
    return (nrRows / CompressionSettings.BITMAP_BLOCK_SZ) * 2;
}

private static int calculateExtraRuns(int nrRows, int nrValues) {
    final double extra = (double) nrRows / Character.MAX_VALUE;
    return (int) ((extra / 2) * nrValues);
}

