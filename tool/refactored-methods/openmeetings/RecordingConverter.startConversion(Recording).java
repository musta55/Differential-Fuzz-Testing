@Override
public void startConversion(Recording r) {
    if (r == null) {
        log.warn("Conversion is NOT started. Recording passed is NULL");
        return;
    }
    ProcessResultList logs = new ProcessResultList();
    List<File> waveFiles = new ArrayList<>();
    try {
        log.debug("recording {}", r.getId());
        validateRecording(r);
        RecordingChunk screenChunk = getValidScreenChunk(r);
        r = updateRecordingStatus(r, Recording.Status.CONVERTING);
        screenChunk = waitForTheStream(screenChunk.getId());
        File wav = createWaveFile(r, logs, screenChunk);
        chunkDao.update(screenChunk);
        String mp4path = mergeAudioVideo(r, logs, screenChunk, wav);
        updateRecordingDimensions(r, logs);
        finalizeRec(r, mp4path, logs);
    } catch (Exception err) {
        log.error("[startConversion]", err);
        r.setStatus(Recording.Status.ERROR);
    } finally {
        postProcess(r, logs);
        postProcess(waveFiles);
        recordingDao.update(r);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void validateRecording(Recording r) throws ConversionException {
    if (Strings.isEmpty(r.getHash())) {
        r.setHash(randomUUID().toString());
    }
}

private RecordingChunk getValidScreenChunk(Recording r) throws ConversionException {
    RecordingChunk screenChunk = chunkDao.getScreenByRecording(r.getId());
    if (screenChunk == null) {
        throw new ConversionException("screenMetaData is Null recordingId " + r.getId());
    }
    if (screenChunk.getStreamStatus() == Status.NONE) {
        printChunkInfo(screenChunk, "StartConversion");
        throw new ConversionException("Stream has not been started, error in recording");
    }
    return screenChunk;
}

private Recording updateRecordingStatus(Recording r, Recording.Status status) {
    r.setStatus(status);
    return recordingDao.update(r);
}

private File createWaveFile(Recording r, ProcessResultList logs, RecordingChunk screenChunk) throws Exception {
    File streamFolder = getStreamFolder(r);
    List<File> waveFiles = new ArrayList<>();
    File wav = new File(streamFolder, screenChunk.getStreamName() + "_FINAL_WAVE.wav");
    createWav(r, logs, streamFolder, waveFiles, wav, null);
    return wav;
}

private String mergeAudioVideo(Recording r, ProcessResultList logs, RecordingChunk screenChunk, File wav) throws Exception {
    String inputScreenFullFlv = getRecordingChunk(r.getRoomId(), screenChunk.getStreamName()).getCanonicalPath();
    return convertToMp4(r, List.of("-itsoffset", formatMillis(diff(screenChunk.getStart(), r.getRecordStart())), "-i", inputScreenFullFlv, "-i", wav.getCanonicalPath()), false, logs);
}

private void updateRecordingDimensions(Recording r, ProcessResultList logs) {
    // will return 100x100 for non-video to be able to play
    Dimension dim = getDimension(logs.getLast().getError(), null);
    if (dim != null) {
        r.setWidth(dim.width());
        r.setHeight(dim.height());
    }
}

