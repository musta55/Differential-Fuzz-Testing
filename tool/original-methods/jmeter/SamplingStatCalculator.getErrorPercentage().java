/**
 * Returns the raw double value of the percentage of samples with errors
 * that were recorded. (Between 0.0 and 1.0)
 *
 * @return the raw double value of the percentage of samples with errors
 *         that were recorded.
 */
public double getErrorPercentage() {
    double rval = 0.0;
    if (calculator.getCount() == 0) {
        return rval;
    }
    rval = (double) getCurrentSample().getErrorCount() / (double) calculator.getCount();
    return rval;
}