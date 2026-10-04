protected void appendValue(ACount<T> ent) {
    if (ent != null) {
        // take the tail first
        ACount<T> current = ent;
        while (current.next() != null) {
            current = current.next();
        }
        // set this tail to null.
        current.setNext(null);
        final int ix = hash(ent.key()) % data.length;
        appendValueWithHandling(ent, ix);
    }
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

