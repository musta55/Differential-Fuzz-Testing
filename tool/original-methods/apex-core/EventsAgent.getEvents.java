public List<EventInfo> getEvents(String appId, Long fromTime, Long toTime, long offset, int limit) {
    List<EventInfo> result = new ArrayList<>();
    String dir = getEventsDirectory(appId);
    if (dir == null) {
        return null;
    }
    try (IndexFileBufferedReader ifbr = new IndexFileBufferedReader(new InputStreamReader(stramAgent.getFileSystem().open(new Path(dir, FSPartFileCollection.INDEX_FILE))), dir)) {
        EventsIndexLine indexLine;
        String lastProcessPartFile = null;
        while ((indexLine = (EventsIndexLine) ifbr.readIndexLine()) != null) {
            if (indexLine.isEndLine) {
                continue;
            }
            lastProcessPartFile = indexLine.partFile;
            if (fromTime != null) {
                if (fromTime > indexLine.endTime) {
                    continue;
                }
            }
            if (toTime != null) {
                if (toTime < indexLine.startTime) {
                    return result;
                }
            }
            try (BufferedReader partBr = new BufferedReader(new InputStreamReader(stramAgent.getFileSystem().open(new Path(dir, indexLine.partFile))))) {
                offset = processPartFile(partBr, fromTime, toTime, offset, limit, result);
                limit -= result.size();
            }
        }
        BufferedReader partBr = null;
        try {
            String extraPartFile = getNextPartFile(lastProcessPartFile);
            if (extraPartFile != null && limit > 0) {
                partBr = new BufferedReader(new InputStreamReader(stramAgent.getFileSystem().open(new Path(dir, extraPartFile))));
                processPartFile(partBr, fromTime, toTime, offset, limit, result);
            }
        } catch (Exception ex) {
            // ignore
        } finally {
            IOUtils.closeQuietly(partBr);
        }
    } catch (Exception ex) {
        LOG.warn("Cannot read events for {}: {}", appId, ex);
    }
    return result;
}