@SuppressWarnings("unchecked")
private long processPartFile(BufferedReader partBr, Long fromTime, Long toTime, long offset, int limit, List<EventInfo> result) throws IOException {
    String partLine;
    while ((partLine = partBr.readLine()) != null) {
        EventInfo ev = new EventInfo();
        int cursor = 0;
        int cursor2;
        cursor2 = partLine.indexOf(':', cursor);
        ev.timestamp = Long.parseLong(partLine.substring(cursor, cursor2));
        cursor = cursor2 + 1;
        cursor2 = partLine.indexOf(':', cursor);
        ev.type = partLine.substring(cursor, cursor2);
        cursor = cursor2 + 1;
        if ((fromTime == null || ev.timestamp >= fromTime) && (toTime == null || ev.timestamp <= toTime)) {
            if (offset > 0) {
                offset--;
            } else if (limit-- > 0) {
                ev.data = new ObjectMapper().readValue(partLine.substring(cursor), HashMap.class);
                ev.id = Long.parseLong(ev.data.get("id"));
                ev.data.remove("id");
                result.add(ev);
            }
        }
    }
    return offset;
}
// ---- helper method(s) introduced by the refactoring ----
private void processPartFiles(String dir, LinkedList<Pair<String, Long>> partFiles, long offset, int limit, List<EventInfo> result) {
    for (Pair<String, Long> partFile : partFiles) {
        try (BufferedReader partBr = new BufferedReader(new InputStreamReader(stramAgent.getFileSystem().open(new Path(dir, partFile.first))))) {
            processPartFile(partBr, null, null, offset, limit, result);
            offset = 0;
        } catch (Exception ex) {
            LOG.warn("Cannot read events for {}: {}", dir, ex);
        }
    }
}

private void processExtraPartFile(String dir, String lastProcessPartFile, long offset, int limit, List<EventInfo> result) {
    BufferedReader partBr = null;
    try {
        String extraPartFile = getNextPartFile(lastProcessPartFile);
        if (extraPartFile != null && limit > 0) {
            partBr = new BufferedReader(new InputStreamReader(stramAgent.getFileSystem().open(new Path(dir, extraPartFile))));
            processPartFile(partBr, null, null, offset, limit, result);
        }
    } catch (Exception ex) {
        // ignore
    } finally {
        IOUtils.closeQuietly(partBr);
    }
}

private String getLastProcessedPartFile(LinkedList<Pair<String, Long>> partFiles) {
    return partFiles.isEmpty() ? null : partFiles.getLast().first;
}

