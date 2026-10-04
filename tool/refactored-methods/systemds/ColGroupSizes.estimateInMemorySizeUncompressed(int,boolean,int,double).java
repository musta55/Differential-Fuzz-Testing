public static long estimateInMemorySizeUncompressed(int nrRows, boolean contiguousColumns, int nrColumns, double sparsity) {
    long size = estimateInMemorySizeGroup(nrColumns, contiguousColumns);
    // reference to MatrixBlock.
    size += 8;
    size += MatrixBlock.estimateSizeInMemory(nrRows, nrColumns, (nrColumns > 1) ? sparsity : 1);
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

