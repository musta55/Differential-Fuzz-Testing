public void forEachStreamTable(BiConsumer<Integer, MaskedOnceArrayList<T>> action) {
    Object[] segments = (Object[]) SEGMENTS.getAcquire(this);
    for (int i = 0; i < segments.length; i++) {
        Object[] segment = (Object[]) ARRAY.getAcquire(segments, i);
        if (segment == null)
            continue;
        for (int j = 0; j < segment.length; j++) {
            @SuppressWarnings("unchecked")
            MaskedOnceArrayList<T> table = (MaskedOnceArrayList<T>) ARRAY.getAcquire(segment, j);
            if (table != null)
                action.accept((i << _segmentBits) | j, table);
        }
    }
}