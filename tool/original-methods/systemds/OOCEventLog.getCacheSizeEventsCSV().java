public static String getCacheSizeEventsCSV() {
    StringBuilder sb = new StringBuilder();
    sb.append("ThreadID,CallerID,Timestamp,ScheduledEvictionSize,CacheSize,PinnedSize,ReadReservedSize\n");
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
        sb.append(_threadIds[i]);
        sb.append(',');
        sb.append(_callerNames.get(_callerIds[i]));
        sb.append(',');
        sb.append(_startTimestamps[i]);
        sb.append(',');
        sb.append(_endTimestamps[i]);
        sb.append(',');
        sb.append(_data[i]);
        sb.append(',');
        sb.append(pinnedSize);
        sb.append(',');
        sb.append(readReservedSize);
        sb.append('\n');
    }
    return sb.toString();
}