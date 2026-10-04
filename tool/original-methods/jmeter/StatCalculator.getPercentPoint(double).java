/**
 * Get the value which %percent% of the values are less than. This works
 * just like median (where median represents the 50% point). A typical
 * desire is to see the 90% point - the value that 90% of the data points
 * are below, the remaining 10% are above.
 *
 * @param percent
 *            number representing the wished percent (between <code>0</code>
 *            and <code>1.0</code>)
 * @return number of values less than the percentage
 */
public T getPercentPoint(float percent) {
    return getPercentPoint((double) percent);
}