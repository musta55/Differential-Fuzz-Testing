@Override
protected EventsIndexLine parseIndexLine(String line) throws JSONException {
    EventsIndexLine info = new EventsIndexLine();
    if (line.startsWith("E")) {
        info.isEndLine = true;
        return info;
    }
    line = line.trim();
    int cursor = 2;
    int cursor2 = line.indexOf(':', cursor);
    info.partFile = line.substring(cursor, cursor2);
    cursor = cursor2 + 1;
    cursor2 = line.indexOf(':', cursor);
    String timeRange = line.substring(cursor, cursor2);
    String[] tmp = timeRange.split("-");
    info.startTime = Long.valueOf(tmp[0]);
    info.endTime = Long.valueOf(tmp[1]);
    cursor = cursor2 + 1;
    info.numEvents = Long.valueOf(line.substring(cursor));
    return info;
}