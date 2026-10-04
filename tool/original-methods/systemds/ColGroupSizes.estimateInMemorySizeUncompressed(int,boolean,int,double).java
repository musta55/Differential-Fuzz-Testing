public static long estimateInMemorySizeUncompressed(int nrRows, boolean contiguousColumns, int nrColumns, double sparsity) {
    long size = 0;
    // Since the Object is a col group the overhead from the Memory Size group is added
    size += estimateInMemorySizeGroup(nrColumns, contiguousColumns);
    // reference to MatrixBlock.
    size += 8;
    size += MatrixBlock.estimateSizeInMemory(nrRows, nrColumns, (nrColumns > 1) ? sparsity : 1);
    return size;
}