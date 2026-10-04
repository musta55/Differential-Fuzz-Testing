/**
 * Create an estimator for the input data with the given settings and parallelization degree.
 *
 * @param data The matrix to extract compression information from.
 * @param cs   The settings for the compression
 * @param k    The parallelization degree
 * @return A new CompressionSizeEstimator used to extract information of column groups
 */
public static AComEst createEstimator(MatrixBlock data, CompressionSettings cs, int k) {
    final int nRows = cs.transposed ? data.getNumColumns() : data.getNumRows();
    final int nCols = cs.transposed ? data.getNumRows() : data.getNumColumns();
    final double sparsity = data.getSparsity();
    final int sampleSize = getSampleSize(cs, nRows, nCols, sparsity);
    if (data instanceof CompressedMatrixBlock)
        return createCompressedEstimator((CompressedMatrixBlock) data, cs, sampleSize, k);
    if (data.isEmpty())
        return createExactEstimator(data, cs);
    return createEstimator(data, cs, sampleSize, k, nRows);
}