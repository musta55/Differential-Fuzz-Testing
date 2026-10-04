public static long estimateInMemorySizeOLE(int nrColumns, boolean contiguousColumns, int nrValues, int offsetLength, int nrRows, double tupleSparsity, boolean lossy) {
    nrColumns = nrColumns > 0 ? nrColumns : 1;
    offsetLength += (nrRows / CompressionSettings.BITMAP_BLOCK_SZ) * 2;
    long size = estimateInMemorySizeOffset(nrColumns, contiguousColumns, nrValues, nrValues + 1, offsetLength, tupleSparsity, lossy);
    return size;
}