/**
 * Create an estimator for the input data with the given settings and parallelization degree.
 *
 * @param data The matrix to extract compression information from.
 * @param cs   The settings for the compression
 * @param k    The parallelization degree
 * @return A new CompressionSizeEstimator used to extract information of column groups
 */
public static AComEst createEstimator(MatrixBlock data, CompressionSettings cs, int k) {
    final int nRows = getNumRows(data, cs);
    final int nCols = getNumColumns(data, cs);
    final double sparsity = data.getSparsity();
    final int sampleSize = getSampleSize(cs, nRows, nCols, sparsity);
    if (data instanceof CompressedMatrixBlock)
        return createCompressedEstimator((CompressedMatrixBlock) data, cs, sampleSize, k);
    if (data.isEmpty())
        return createExactEstimator(data, cs);
    return createEstimator(data, cs, sampleSize, k, nRows);
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

