/**
 * Get sampleSize based on compression settings.
 *
 * @param cs       The compression settings
 * @param nRows    Number of rows in input
 * @param nCols    Number of columns in input
 * @param sparsity The sparsity of the input
 * @return a sample size
 */
private static int getSampleSize(CompressionSettings cs, int nRows, int nCols, double sparsity) {
    final int maxSize = Math.min(cs.maxSampleSize, nRows);
    return getSampleSize(cs.samplePower, nRows, nCols, sparsity, cs.minimumSampleSize, maxSize);
}