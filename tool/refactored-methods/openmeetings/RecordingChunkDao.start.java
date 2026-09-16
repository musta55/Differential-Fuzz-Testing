public Long start(Long recordingId, Type type, String streamName, String sid) {
    RecordingChunk chunk = createRecordingChunk(recordingId, type, streamName, sid);
    persistChunk(chunk);
    return chunk.getId();
}
// ---- helper method(s) introduced by the refactoring ----
private RecordingChunk createRecordingChunk(Long recordingId, Type type, String streamName, String sid) {
    RecordingChunk chunk = new RecordingChunk();
    chunk.setRecording(recordingDao.get(recordingId));
    chunk.setStart(new Date());
    chunk.setType(type);
    chunk.setStreamName(streamName);
    chunk.setStreamStatus(Status.STARTED);
    chunk.setSid(sid);
    return chunk;
}

private void persistChunk(RecordingChunk chunk) {
    chunk = update(chunk);
}

private void updateChunkStatus(RecordingChunk chunk, Date date, Status status) {
    chunk.setEnd(date);
    chunk.setStreamStatus(status);
    update(chunk);
}

