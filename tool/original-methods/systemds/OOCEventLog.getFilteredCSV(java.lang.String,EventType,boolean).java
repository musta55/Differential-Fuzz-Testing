private static String getFilteredCSV(String header, EventType filter, boolean data) {
    StringBuilder sb = new StringBuilder();
    sb.append(header);
    int maxIdx = Math.min(_logCtr.get(), _eventTypes.length);
    for (int i = 0; i < maxIdx; i++) {
        if (_eventTypes[i] != filter)
            continue;
        sb.append(_threadIds[i]);
        sb.append(',');
        sb.append(_callerNames.get(_callerIds[i]));
        sb.append(',');
        sb.append(_startTimestamps[i]);
        sb.append(',');
        sb.append(_endTimestamps[i]);
        if (data) {
            sb.append(',');
            sb.append(_data[i]);
        }
        sb.append('\n');
    }
    return sb.toString();
}