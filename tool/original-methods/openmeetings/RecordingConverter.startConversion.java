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
        File streamFolder = getStreamFolder(r);
        RecordingChunk screenChunk = chunkDao.getScreenByRecording(r.getId());
        if (screenChunk == null) {
            throw new ConversionException("screenMetaData is Null recordingId " + r.getId());
        }
        if (screenChunk.getStreamStatus() == Status.NONE) {
            printChunkInfo(screenChunk, "StartConversion");
            throw new ConversionException("Stream has not been started, error in recording");
        }
        if (Strings.isEmpty(r.getHash())) {
            r.setHash(randomUUID().toString());
        }
        r.setStatus(Recording.Status.CONVERTING);
        r = recordingDao.update(r);
        screenChunk = waitForTheStream(screenChunk.getId());
        // Merge Wave to Full Length
        File wav = new File(streamFolder, screenChunk.getStreamName() + "_FINAL_WAVE.wav");
        createWav(r, logs, streamFolder, waveFiles, wav, null);
        chunkDao.update(screenChunk);
        // Merge Audio with Video / Calculate resulting FLV
        String inputScreenFullFlv = getRecordingChunk(r.getRoomId(), screenChunk.getStreamName()).getCanonicalPath();
        // ffmpeg -vcodec flv -qscale 9.5 -r 25 -ar 22050 -ab 32k -s 320x240
        // -i 65318fb5c54b1bc1b1bca077b493a914_28_12_2009_23_38_17_FINAL_WAVE.wav
        // -i 65318fb5c54b1bc1b1bca077b493a914_28_12_2009_23_38_17.flv
        // final1.flv
        String mp4path = convertToMp4(r, List.of("-itsoffset", formatMillis(diff(screenChunk.getStart(), r.getRecordStart())), "-i", inputScreenFullFlv, "-i", wav.getCanonicalPath()), false, logs);
        // will return 100x100 for non-video to be able to play
        Dimension dim = getDimension(logs.getLast().getError(), null);
        if (dim != null) {
            r.setWidth(dim.width());
            r.setHeight(dim.height());
        }
        finalizeRec(r, mp4path, logs);
    } catch (Exception err) {
        log.error("[startConversion]", err);
        r.setStatus(Recording.Status.ERROR);
    }
    postProcess(r, logs);
    postProcess(waveFiles);
    recordingDao.update(r);
}