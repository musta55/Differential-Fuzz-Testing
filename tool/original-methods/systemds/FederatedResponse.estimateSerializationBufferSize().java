public long estimateSerializationBufferSize() {
    // general offset for the FederatedResponse object
    long minBufferSize = 312;
    if (_data != null) {
        for (Object obj : _data) {
            if (obj instanceof CacheBlock)
                minBufferSize += ((CacheBlock<?>) obj).getExactSerializedSize();
        }
    }
    return minBufferSize;
}