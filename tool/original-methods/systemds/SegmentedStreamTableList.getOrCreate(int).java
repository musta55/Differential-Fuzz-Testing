public MaskedOnceArrayList<T> getOrCreate(int streamId) {
    checkStreamId(streamId);
    int segmentIndex = segmentIndex(streamId);
    int offset = offsetInSegment(streamId);
    while (true) {
        Object[] segments = ensureOuterCapacity(segmentIndex + 1);
        Object[] segment = (Object[]) ARRAY.getAcquire(segments, segmentIndex);
        if (segment == null) {
            Object[] newSegment = new Object[_segmentSize];
            if (!ARRAY.compareAndSet(segments, segmentIndex, null, newSegment))
                continue;
            segment = newSegment;
        }
        @SuppressWarnings("unchecked")
        MaskedOnceArrayList<T> streamTable = (MaskedOnceArrayList<T>) ARRAY.getAcquire(segment, offset);
        if (streamTable != null)
            return streamTable;
        MaskedOnceArrayList<T> newTable = new MaskedOnceArrayList<>(_streamPartitionSize);
        if (ARRAY.compareAndSet(segment, offset, null, newTable))
            return newTable;
    }
}