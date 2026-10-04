public static void onCacheSizeChangedEvent(int callerId, long timestamp, long cacheSize, long bytesToEvict, long pinnedBytes, long readingReservedBytes) {
    int idx = _logCtr.getAndAdd(2);
    if (idx + 1 >= _eventTypes.length)
        return;
    logCacheSizeChangeEvent(idx, callerId, timestamp, cacheSize, bytesToEvict);
    logCacheSizeChangeEvent(idx + 1, callerId, timestamp, pinnedBytes, readingReservedBytes);
}
// ---- helper method(s) introduced by the refactoring ----
private static void logEvent(EventType eventType, int callerId, long startTimestamp, long endTimestamp, long dataSize) {
    int idx = _logCtr.getAndIncrement();
    if (idx >= _eventTypes.length)
        return;
    _eventTypes[idx] = eventType;
    _startTimestamps[idx] = startTimestamp;
    _endTimestamps[idx] = endTimestamp;
    _callerIds[idx] = callerId;
    _threadIds[idx] = Thread.currentThread().getId();
    _data[idx] = dataSize;
}

private static void logCacheSizeChangeEvent(int idx, int callerId, long timestamp, long data, long endTimestamp) {
    _eventTypes[idx] = EventType.CACHESIZE_CHANGE;
    _startTimestamps[idx] = timestamp;
    _endTimestamps[idx] = endTimestamp;
    _callerIds[idx] = callerId;
    _threadIds[idx] = Thread.currentThread().getId();
    _data[idx] = data;
}

private static void appendCacheSizeEvent(StringBuilder sb, int idx, long pinnedSize, long readReservedSize) {
    sb.append(_threadIds[idx]).append(',').append(_callerNames.get(_callerIds[idx])).append(',').append(_startTimestamps[idx]).append(',').append(_endTimestamps[idx]).append(',').append(_data[idx]).append(',').append(pinnedSize).append(',').append(readReservedSize).append('\n');
}

private static void appendEvent(StringBuilder sb, int idx, boolean includeData) {
    sb.append(_threadIds[idx]).append(',').append(_callerNames.get(_callerIds[idx])).append(',').append(_startTimestamps[idx]).append(',').append(_endTimestamps[idx]);
    if (includeData) {
        sb.append(',').append(_data[idx]);
    }
    sb.append('\n');
}

