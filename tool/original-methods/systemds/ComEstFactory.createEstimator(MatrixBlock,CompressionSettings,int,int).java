/**
 * Create an estimator for the input data with the given settings and parallelization degree.
 *
 * @param data       The matrix to extract compression information from.
 * @param cs         The settings for the compression
 * @param sampleSize The number of rows to extract from the input data to extract information from.
 * @param k          The parallelization degree
 * @return A new CompressionSizeEstimator used to extract information of column groups
 */
public static AComEst createEstimator(MatrixBlock data, CompressionSettings cs, int sampleSize, int k) {
    final int nRows = cs.transposed ? data.getNumColumns() : data.getNumRows();
    return createEstimator(data, cs, sampleSize, k, nRows);
}