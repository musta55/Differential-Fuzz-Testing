public static void onComputeEvent(int callerId, long startTimestamp, long endTimestamp) {
    int idx = _logCtr.getAndIncrement();
    if (idx >= _eventTypes.length)
        return;
    _eventTypes[idx] = EventType.COMPUTE;
    _startTimestamps[idx] = startTimestamp;
    _endTimestamps[idx] = endTimestamp;
    _callerIds[idx] = callerId;
    _threadIds[idx] = Thread.currentThread().getId();
}