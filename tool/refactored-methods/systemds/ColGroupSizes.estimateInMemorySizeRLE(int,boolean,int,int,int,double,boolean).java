public static long estimateInMemorySizeRLE(int nrColumns, boolean contiguousColumns, int nrValues, int nrRuns, int nrRows, double tupleSparsity, boolean lossy) {
    if (nrRows > Character.MAX_VALUE) {
        nrRuns += calculateExtraRuns(nrRows, nrValues);
    }
    return estimateInMemorySizeOffset(nrColumns, contiguousColumns, nrValues, nrValues + 1, nrRuns * 2, tupleSparsity, lossy);
}
// ---- helper method(s) introduced by the refactoring ----
private static int calculateAdditionalOffsetLength(int nrRows) {
    return (nrRows / CompressionSettings.BITMAP_BLOCK_SZ) * 2;
}

private static int calculateExtraRuns(int nrRows, int nrValues) {
    final double extra = (double) nrRows / Character.MAX_VALUE;
    return (int) ((extra / 2) * nrValues);
}

