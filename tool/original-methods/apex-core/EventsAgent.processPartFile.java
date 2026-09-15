@SuppressWarnings("unchecked")
private long processPartFile(BufferedReader partBr, Long fromTime, Long toTime, long offset, int limit, List<EventInfo> result) throws IOException {
    String partLine;
    while ((partLine = partBr.readLine()) != null) {
        EventInfo ev = new EventInfo();
        int cursor = 0;
        int cursor2;
        cursor2 = partLine.indexOf(':', cursor);
        ev.timestamp = Long.valueOf(partLine.substring(cursor, cursor2));
        cursor = cursor2 + 1;
        cursor2 = partLine.indexOf(':', cursor);
        ev.type = partLine.substring(cursor, cursor2);
        cursor = cursor2 + 1;
        if ((fromTime == null || ev.timestamp >= fromTime) && (toTime == null || ev.timestamp <= toTime)) {
            if (offset > 0) {
                offset--;
            } else if (limit-- > 0) {
                ev.data = new ObjectMapper().readValue(partLine.substring(cursor), HashMap.class);
                ev.id = Long.valueOf(ev.data.get("id"));
                ev.data.remove("id");
                result.add(ev);
            }
        }
    }
    return offset;
}