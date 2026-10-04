/**
 * Increments the reference counter of a stream by the set amount.
 */
public static void incrRef(OOCStreamable<IndexedMatrixValue> stream, int incr) {
    if (!stream.hasStreamCache())
        return;
    CachingStream cache = stream.getStreamCache();
    Integer ref = refCtr.compute(cache, (k, v) -> {
        if (v == null)
            v = 0;
        v += incr;
        return v <= 0 ? null : v;
    });
    if (ref == null)
        cache.scheduleDeletion();
}