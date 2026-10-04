public MaskedOnceArrayList<T> getOrCreate(int streamId) {
    checkStreamId(streamId);
    int segmentIndex = segmentIndex(streamId);
    int offset = offsetInSegment(streamId);
    while (true) {
        Object[] segments = ensureOuterCapacity(segmentIndex + 1);
        Object[] segment = (Object[]) ARRAY.getAcquire(segments, segmentIndex);
        if (isSegmentNull(segment)) {
            Object[] newSegment = new Object[_segmentSize];
            if (!ARRAY.compareAndSet(segments, segmentIndex, null, newSegment))
                continue;
            segment = newSegment;
        }
        @SuppressWarnings("unchecked")
        MaskedOnceArrayList<T> streamTable = (MaskedOnceArrayList<T>) ARRAY.getAcquire(segment, offset);
        if (isStreamTableNotNull(streamTable))
            return streamTable;
        MaskedOnceArrayList<T> newTable = new MaskedOnceArrayList<>(_streamPartitionSize);
        if (ARRAY.compareAndSet(segment, offset, null, newTable))
            return newTable;
    }
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

