public static long estimateInMemorySizeRLE(int nrColumns, boolean contiguousColumns, int nrValues, int nrRuns, int nrRows, double tupleSparsity, boolean lossy) {
    // Correct low number of runs if very large input.
    // This correction handles the case where the skip runs are added in a safe manner
    if (nrRows > Character.MAX_VALUE) {
        final double extra = (double) nrRows / Character.MAX_VALUE;
        // we assume that half unique values contain extra runs if we have few runs to begin with.
        // This is not 100% guaranteeing larger estimate than real but most likely
        nrRuns += (extra / 2) * nrValues;
    }
    return estimateInMemorySizeOffset(nrColumns, contiguousColumns, nrValues, nrValues + 1, nrRuns * 2, tupleSparsity, lossy);
}