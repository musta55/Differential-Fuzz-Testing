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
    // Start sample size at the min sample size as the basis sample.
    int sampleSize = minSampleSize;
    // ensure samplePower is in valid range
    samplePower = Math.max(0, Math.min(1, samplePower));
    // Scale the sample size with the number of rows in the input.
    // Sub linearly since the the number of rows needed to classify the contained values in a population doesn't scale
    // linearly.
    sampleSize += (int) Math.ceil(Math.pow(nRows, samplePower));
    // Scale sample size based on overall sparsity so that if the input is very sparse, increase the sample size.
    sampleSize = (int) (sampleSize * (1.0 / Math.min(sparsity + 0.2, 1.0)));
    // adhere to maximum sample size.
    sampleSize = Math.max(minSampleSize, Math.min(sampleSize, maxSampleSize));
    // cap at number of rows.
    sampleSize = Math.min(nRows, sampleSize);
    return sampleSize;
}