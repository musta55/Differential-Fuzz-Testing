public long estimateSerializationBufferSize() {
    // general offset for the FederatedResponse object
    long minBufferSize = 312;
    if (_data != null) {
        minBufferSize += calculateDataSize(_data);
    }
    return minBufferSize;
}
// ---- helper method(s) introduced by the refactoring ----
private long calculateDataSize(Object[] data) {
    long size = 0;
    for (Object obj : data) {
        if (obj instanceof CacheBlock)
            size += ((CacheBlock<?>) obj).getExactSerializedSize();
    }
    return size;
}

