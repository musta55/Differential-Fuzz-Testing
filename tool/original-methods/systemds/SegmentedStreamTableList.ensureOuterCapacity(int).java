private Object[] ensureOuterCapacity(int minLength) {
    Object[] segments = (Object[]) SEGMENTS.getAcquire(this);
    while (minLength > segments.length) {
        int newLength = segments.length;
        while (newLength < minLength) {
            if (newLength > Integer.MAX_VALUE / 2)
                throw new IllegalStateException("SegmentedStreamTableList capacity overflow");
            newLength <<= 1;
        }
        Object[] bigger = new Object[newLength];
        System.arraycopy(segments, 0, bigger, 0, segments.length);
        if (SEGMENTS.compareAndSet(this, segments, bigger))
            return bigger;
        segments = (Object[]) SEGMENTS.getAcquire(this);
    }
    return segments;
}