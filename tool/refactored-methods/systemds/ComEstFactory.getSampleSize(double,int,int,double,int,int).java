/**
 * This function returns the sample size to use.
 *
 * The sampling is bound by the maximum sampling and the minimum sampling.
 *
 * The sampling is calculated based on the a power of the number of rows and a sampling fraction
 *
 * @param samplePower   The sample power
 * @param nRows         The number of rows
 * @param nCols         The number of columns
 * @param sparsity      The sparsity of the input
 * @param minSampleSize The minimum sample size
 * @param maxSampleSize The maximum sample size
 * @return The sample size to use.
 */
public static int getSampleSize(double samplePower, int nRows, int nCols, double sparsity, int minSampleSize, int maxSampleSize) {
    return calculateSampleSize(samplePower, nRows, nCols, sparsity, minSampleSize, maxSampleSize);
}
// ---- helper method(s) introduced by the refactoring ----
private static int getNumRows(MatrixBlock data, CompressionSettings cs) {
    return cs.transposed ? data.getNumColumns() : data.getNumRows();
}

private static int getNumColumns(MatrixBlock data, CompressionSettings cs) {
    return cs.transposed ? data.getNumRows() : data.getNumColumns();
}

private static boolean isLargeSample(int sampleSize, int nRows) {
    return sampleSize >= nRows * 0.8;
}

private static int getMaxSampleSize(CompressionSettings cs, int nRows) {
    return Math.min(cs.maxSampleSize, nRows);
}

private static int calculateSampleSize(double samplePower, int nRows, int nCols, double sparsity, int minSampleSize, int maxSampleSize) {
    int sampleSize = minSampleSize;
    samplePower = Math.max(0, Math.min(1, samplePower));
    sampleSize += (int) Math.ceil(Math.pow(nRows, samplePower));
    sampleSize = (int) (sampleSize * (1.0 / Math.min(sparsity + 0.2, 1.0)));
    sampleSize = Math.max(minSampleSize, Math.min(sampleSize, maxSampleSize));
    sampleSize = Math.min(nRows, sampleSize);
    return sampleSize;
}

