public void stop(Long chunkId) {
    RecordingChunk chunk = get(chunkId);
    if (chunk != null) {
        chunk.setEnd(new Date());
        chunk.setStreamStatus(Status.STOPPED);
        update(chunk);
    }
}