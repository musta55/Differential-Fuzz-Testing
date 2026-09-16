/**
 * Get the value which %percent% of the values are less than. This works
 * just like median (where median represents the 50% point). A typical
 * desire is to see the 90% point - the value that 90% of the data points
 * are below, the remaining 10% are above.
 *
 * @param percent
 *            number representing the wished percent (between <code>0</code>
 *            and <code>1.0</code>)
 * @return the value which %percent% of the values are less than
 */
public T getPercentPoint(double percent) {
    if (count <= 0 || percent >= 1.0) {
        return percent >= 1.0 ? getMax() : zero;
    }
    long target = Math.round(count * percent);
    try {
        for (Map.Entry<T, MutableLong> val : valuesMap.entrySet()) {
            target -= val.getValue().longValue();
            if (target <= 0) {
                return val.getKey();
            }
        }
    } catch (ConcurrentModificationException ignored) {
        // ignored. May happen occasionally, but no harm done if so.
    }
    // TODO should this be getMin()?
    return zero;
}