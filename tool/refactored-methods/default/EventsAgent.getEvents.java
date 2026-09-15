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
            if (fromTime != null && fromTime > indexLine.endTime) {
                continue;
            }
            if (toTime != null && toTime < indexLine.startTime) {
                return result;
            }
            try (BufferedReader partBr = new BufferedReader(new InputStreamReader(stramAgent.getFileSystem().open(new Path(dir, indexLine.partFile))))) {
                offset = processPartFile(partBr, fromTime, toTime, offset, limit, result);
                limit -= result.size();
            }
        }
        processExtraPartFile(dir, lastProcessPartFile, offset, limit, result);
    } catch (Exception ex) {
        LOG.warn("Cannot read events for {}: {}", appId, ex);
    }
    return result;
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

