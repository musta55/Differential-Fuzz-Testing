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
    T actualValue = sampleCount > 1 ? divide(val, sampleCount) : val;
    sumOfSquares += currentVal * currentVal / sampleCount;
    updateValueCount(actualValue, sampleCount);
    calculateDerivedValues(actualValue);
}