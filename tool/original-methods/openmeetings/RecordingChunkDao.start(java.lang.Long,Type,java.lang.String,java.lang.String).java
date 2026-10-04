public Long start(Long recordingId, Type type, String streamName, String sid) {
    RecordingChunk chunk = new RecordingChunk();
    chunk.setRecording(recordingDao.get(recordingId));
    chunk.setStart(new Date());
    chunk.setType(type);
    chunk.setStreamName(streamName);
    chunk.setStreamStatus(Status.STARTED);
    chunk.setSid(sid);
    chunk = update(chunk);
    return chunk.getId();
}