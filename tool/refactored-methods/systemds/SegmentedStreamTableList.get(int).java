public MaskedOnceArrayList<T> get(int streamId) {
    checkStreamId(streamId);
    Object[] segments = (Object[]) SEGMENTS.getAcquire(this);
    int segmentIndex = segmentIndex(streamId);
    if (isSegmentIndexValid(segments, segmentIndex))
        return null;
    Object[] segment = (Object[]) ARRAY.getAcquire(segments, segmentIndex);
    if (isSegmentNull(segment))
        return null;
    @SuppressWarnings("unchecked")
    MaskedOnceArrayList<T> streamTable = (MaskedOnceArrayList<T>) ARRAY.getAcquire(segment, offsetInSegment(streamId));
    return streamTable;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSegmentIndexValid(Object[] segments, int segmentIndex) {
    return segmentIndex >= segments.length;
}

private boolean isSegmentNull(Object[] segment) {
    return segment == null;
}

private boolean isStreamTableNotNull(MaskedOnceArrayList<T> streamTable) {
    return streamTable != null;
}

private Object[] expandSegments(Object[] segments, int minLength) {
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
    return (Object[]) SEGMENTS.getAcquire(this);
}

private void processSegments(Object[] segments, BiConsumer<Object[], Integer> segmentProcessor) {
    for (int i = 0; i < segments.length; i++) {
        Object[] segment = (Object[]) ARRAY.getAcquire(segments, i);
        if (segment == null)
            continue;
        segmentProcessor.accept(segment, i);
    }
}

private void processSegment(Object[] segment, int index, BiConsumer<Integer, MaskedOnceArrayList<T>> action) {
    for (int j = 0; j < segment.length; j++) {
        @SuppressWarnings("unchecked")
        MaskedOnceArrayList<T> table = (MaskedOnceArrayList<T>) ARRAY.getAcquire(segment, j);
        if (table != null)
            action.accept((index << _segmentBits) | j, table);
    }
}

private void processSegment(Object[] segment, int index, Consumer<MaskedOnceArrayList<T>> action) {
    for (int j = 0; j < segment.length; j++) {
        @SuppressWarnings("unchecked")
        MaskedOnceArrayList<T> table = (MaskedOnceArrayList<T>) ARRAY.getAcquire(segment, j);
        if (table != null)
            action.accept(table);
    }
}

