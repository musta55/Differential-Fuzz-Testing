public synchronized final int increment(final double key, final int count) {
    // skip hash if data array is 1 length
    final int ix = data.length < shortCutSize ? 0 : DCounts.hashIndex(key) % data.length;
    try {
        return increment(key, ix, count);
    } catch (ArrayIndexOutOfBoundsException e) {
        if (ix < 0)
            return increment(key, 0, count);
        else
            throw new RuntimeException(e);
    }
}