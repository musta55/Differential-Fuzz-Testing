/**
 * Peter J. Haas, Jeffrey F. Naughton, S. Seshadri, and Lynne Stokes. Sampling-Based Estimation of the Number of
 * Distinct Values of an Attribute. VLDB'95, Section 3.2.
 *
 * @param numVals    The number of unique values in the sample
 * @param freqCounts The inverse histogram of frequencies. counts extracted
 * @param nRows      The original number of rows in the entire input
 * @param sampleSize The number of rows in the sample
 * @return an estimation of number of distinct values.
 */
public static int distinctCount(long numVals, int[] freqCounts, long nRows, long sampleSize) {
    if (// early abort
    freqCounts[0] == 0)
        return (int) numVals;
    final double q = ((double) sampleSize) / nRows;
    final double oneMinusQ = 1 - q;
    double numberSum = 0, denomSum = 0;
    int i = 0;
    while (i < freqCounts.length) {
        double p1 = calculateP1(oneMinusQ, i, freqCounts[i]);
        numberSum += p1 * oneMinusQ;
        denomSum += (++i) * q * p1;
    }
    return (int) Math.round(numVals + freqCounts[0] * numberSum / denomSum);
}
// ---- helper method(s) introduced by the refactoring ----
private static double calculateP1(double oneMinusQ, int i, int freqCount) {
    return Math.pow(oneMinusQ, i) * freqCount;
}

