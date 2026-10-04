public static String getCacheSizeEventsCSV() {
    StringBuilder sb = new StringBuilder("ThreadID,CallerID,Timestamp,ScheduledEvictionSize,CacheSize,PinnedSize,ReadReservedSize\n");
    int maxIdx = Math.min(_logCtr.get(), _eventTypes.length);
    for (int i = 0; i < maxIdx; i++) {
        if (_eventTypes[i] != EventType.CACHESIZE_CHANGE)
            continue;
        long pinnedSize = 0;
        long readReservedSize = 0;
        if (i + 1 < maxIdx && _eventTypes[i + 1] == EventType.CACHESIZE_CHANGE_CONT) {
            pinnedSize = _endTimestamps[i + 1];
            readReservedSize = _data[i + 1];
        }
        appendCacheSizeEvent(sb, i, pinnedSize, readReservedSize);
    }
    return sb.toString();
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

