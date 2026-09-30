public List<EventInfo> getLatestEvents(String appId, int limit) {
    LinkedList<EventInfo> result = new LinkedList<>();
    String dir = getEventsDirectory(appId);
    if (dir == null) {
        return null;
    }
    long totalNumEvents = 0;
    LinkedList<Pair<String, Long>> partFiles = new LinkedList<>();
    try (IndexFileBufferedReader ifbr = new IndexFileBufferedReader(new InputStreamReader(stramAgent.getFileSystem().open(new Path(dir, FSPartFileCollection.INDEX_FILE))), dir)) {
        EventsIndexLine indexLine;
        while ((indexLine = (EventsIndexLine) ifbr.readIndexLine()) != null) {
            if (indexLine.isEndLine) {
                continue;
            }
            partFiles.add(new Pair<>(indexLine.partFile, indexLine.numEvents));
            totalNumEvents += indexLine.numEvents;
        }
    } catch (Exception ex) {
        LOG.warn("Cannot read events for {}: {}", appId, ex);
        return result;
    }
    long offset = 0;
    while (totalNumEvents > limit && !partFiles.isEmpty()) {
        Pair<String, Long> head = partFiles.getFirst();
        if (totalNumEvents - head.second < limit) {
            offset = Math.max(0, totalNumEvents - limit);
            break;
        }
        totalNumEvents -= head.second;
        partFiles.removeFirst();
    }
    processPartFiles(dir, partFiles, offset, limit, result);
    processExtraPartFile(dir, getLastProcessedPartFile(partFiles), offset, limit, result);
    while (result.size() > limit) {
        result.removeFirst();
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

