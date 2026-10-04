private static AComEst createEstimator(MatrixBlock data, CompressionSettings cs, int sampleSize, int k, int nRows) {
    if (// if sample size is larger than 80% use entire input as sample.
    sampleSize >= nRows * 0.8)
        return createExactEstimator(data, cs);
    else
        return createSampleEstimator(data, cs, sampleSize, k);
}