public static long estimateInMemorySizeSDC(int nrColumns, boolean contiguousColumns, int nrValues, int nrRows, int largestOff, double tupleSparsity, boolean largestOffZero, boolean lossy) {
    long size = estimateInMemorySizeGroupValue(nrColumns, contiguousColumns, nrValues, tupleSparsity, lossy);
    size += OffsetFactory.estimateInMemorySize(nrRows - largestOff, nrRows);
    if (nrValues > 1 + (largestOffZero ? 0 : 1))
        size += MapToFactory.estimateInMemorySize(nrRows - largestOff, nrValues);
    return size;
}