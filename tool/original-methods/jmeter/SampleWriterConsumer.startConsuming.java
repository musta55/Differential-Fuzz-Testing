@Override
public void startConsuming() {
    if (outputFile == null) {
        File wd = getWorkingDirectory();
        wd.mkdir();
        if (LOG.isInfoEnabled()) {
            LOG.info("startConsuming(): No output file set, writing to work directory :" + wd.getAbsolutePath());
        }
        outputFile = new File(wd, "samples.csv");
    }
    outputFile.getParentFile().mkdirs();
    channelsCount = getConsumedChannelCount();
    csvWriters = new CsvSampleWriter[channelsCount];
    for (int i = 0; i < channelsCount; i++) {
        csvWriters[i] = new CsvSampleWriter(getOutputFile(i), getConsumedMetadata(i));
        if (shouldWriteHeader) {
            csvWriters[i].writeHeader();
        }
    }
}