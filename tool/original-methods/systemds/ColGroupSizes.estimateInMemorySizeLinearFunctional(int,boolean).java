public static long estimateInMemorySizeLinearFunctional(int nrColumns, boolean contiguousColumns) {
    long size = 0;
    // Since the Object is a col group the overhead from the Memory Size group is added
    size += estimateInMemorySizeGroup(nrColumns, contiguousColumns);
    // coefficients; per column, we store 2 doubles (slope &
    size += MemoryEstimates.doubleArrayCost(2L * nrColumns);
    // intercept)
    // _numRows
    size += 4;
    return size;
}