public static long estimateInMemorySizeSDC(int nrColumns, boolean contiguousColumns, int nrValues, int nrRows, int largestOff, double tupleSparsity, boolean largestOffZero, boolean lossy) {
    long size = estimateInMemorySizeGroupValue(nrColumns, contiguousColumns, nrValues, tupleSparsity, lossy);
    size += OffsetFactory.estimateInMemorySize(nrRows - largestOff, nrRows);
    if (nrValues > 1 + (largestOffZero ? 0 : 1)) {
        size += MapToFactory.estimateInMemorySize(nrRows - largestOff, nrValues);
    }
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

