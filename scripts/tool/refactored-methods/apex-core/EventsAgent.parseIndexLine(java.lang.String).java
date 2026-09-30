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
    info.startTime = Long.parseLong(tmp[0]);
    info.endTime = Long.parseLong(tmp[1]);
    cursor = cursor2 + 1;
    info.numEvents = Long.parseLong(line.substring(cursor));
    return info;
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

