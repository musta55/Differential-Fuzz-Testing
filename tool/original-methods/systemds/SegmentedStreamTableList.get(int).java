public MaskedOnceArrayList<T> get(int streamId) {
    checkStreamId(streamId);
    Object[] segments = (Object[]) SEGMENTS.getAcquire(this);
    int segmentIndex = segmentIndex(streamId);
    if (segmentIndex >= segments.length)
        return null;
    Object[] segment = (Object[]) ARRAY.getAcquire(segments, segmentIndex);
    if (segment == null)
        return null;
    @SuppressWarnings("unchecked")
    MaskedOnceArrayList<T> streamTable = (MaskedOnceArrayList<T>) ARRAY.getAcquire(segment, offsetInSegment(streamId));
    return streamTable;
}