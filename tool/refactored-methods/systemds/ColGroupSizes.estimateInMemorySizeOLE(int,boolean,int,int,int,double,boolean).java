public static long estimateInMemorySizeOLE(int nrColumns, boolean contiguousColumns, int nrValues, int offsetLength, int nrRows, double tupleSparsity, boolean lossy) {
    nrColumns = Math.max(nrColumns, 1);
    offsetLength += calculateAdditionalOffsetLength(nrRows);
    return estimateInMemorySizeOffset(nrColumns, contiguousColumns, nrValues, nrValues + 1, offsetLength, tupleSparsity, lossy);
}
// ---- helper method(s) introduced by the refactoring ----
private static int calculateAdditionalOffsetLength(int nrRows) {
    return (nrRows / CompressionSettings.BITMAP_BLOCK_SZ) * 2;
}

private static int calculateExtraRuns(int nrRows, int nrValues) {
    final double extra = (double) nrRows / Character.MAX_VALUE;
    return (int) ((extra / 2) * nrValues);
}

