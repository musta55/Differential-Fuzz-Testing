/**
 * Increment and return the id of the incremented index.
 *
 * @param key   The key to increment
 * @param count The number of times to increment the value
 * @return The Id of the incremented entry.
 */
public synchronized int increment(final T key, final int count) {
    // skip hash if data array is 1 length
    final int ix = data.length < shortCutSize ? 0 : hash(key) % data.length;
    try {
        return increment(key, ix, count);
    } catch (ArrayIndexOutOfBoundsException e) {
        if (ix < 0)
            return increment(key, 0, count);
        else
            throw new RuntimeException(e);
    }
}