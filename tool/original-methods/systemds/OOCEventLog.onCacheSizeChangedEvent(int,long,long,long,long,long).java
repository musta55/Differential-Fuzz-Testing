public static void onCacheSizeChangedEvent(int callerId, long timestamp, long cacheSize, long bytesToEvict, long pinnedBytes, long readingReservedBytes) {
    int idx = _logCtr.getAndAdd(2);
    if (idx + 1 >= _eventTypes.length)
        return;
    _eventTypes[idx] = EventType.CACHESIZE_CHANGE;
    _startTimestamps[idx] = timestamp;
    _endTimestamps[idx] = bytesToEvict;
    _callerIds[idx] = callerId;
    _threadIds[idx] = Thread.currentThread().getId();
    _data[idx] = cacheSize;
    int idxCont = idx + 1;
    _eventTypes[idxCont] = EventType.CACHESIZE_CHANGE_CONT;
    _startTimestamps[idxCont] = timestamp;
    _endTimestamps[idxCont] = pinnedBytes;
    _callerIds[idxCont] = callerId;
    _threadIds[idxCont] = Thread.currentThread().getId();
    _data[idxCont] = readingReservedBytes;
}