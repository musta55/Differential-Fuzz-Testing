public ACountHashMap() {
    this(1);
}
// ---- helper method(s) introduced by the refactoring ----
private final int incrementWithHandling(final T key, final int ix, final int count) {
    try {
        return increment(key, ix, count);
    } catch (ArrayIndexOutOfBoundsException e) {
        if (ix < 0)
            return increment(key, 0, count);
        else
            throw new RuntimeException(e);
    }
}

private final int incrementWithHandling(final double key, final int ix, final int count) {
    try {
        return increment(key, ix, count);
    } catch (ArrayIndexOutOfBoundsException e) {
        if (ix < 0)
            return increment(key, 0, count);
        else
            throw new RuntimeException(e);
    }
}

private void appendValueWithHandling(ACount<T> ent, int ix) {
    try {
        appendValue(ent, ix);
    } catch (ArrayIndexOutOfBoundsException e) {
        if (ix < 0)
            appendValue(ent, 0);
        else
            throw new RuntimeException(e);
    }
}

