/**
 * Update the calculator with the value for an aggregated sample.
 *
 * @param val the aggregate value, normally the elapsed time
 * @param sampleCount the number of samples contributing to the aggregate value
 */
public void addValue(T val, long sampleCount) {
    count += sampleCount;
    double currentVal = val.doubleValue();
    sum += currentVal;
    T actualValue = val;
    if (sampleCount > 1) {
        // For n values in an aggregate sample the average value = (val/n)
        // So need to add n * (val/n) * (val/n) = val * val / n
        sumOfSquares += currentVal * currentVal / sampleCount;
        actualValue = divide(val, sampleCount);
    } else {
        // no need to divide by 1
        sumOfSquares += currentVal * currentVal;
    }
    updateValueCount(actualValue, sampleCount);
    calculateDerivedValues(actualValue);
}