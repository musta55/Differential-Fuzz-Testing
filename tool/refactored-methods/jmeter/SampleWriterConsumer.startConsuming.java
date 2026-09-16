@Override
public void startConsuming() {
    if (outputFile == null) {
        File wd = getWorkingDirectory();
        wd.mkdirs();
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
// ---- helper method(s) introduced by the refactoring ----
private static String[] splitFileNameAndExtension(String fileName) {
    int idx = fileName.lastIndexOf('.');
    if (idx >= 0 && idx < fileName.length() - 1) {
        return new String[] { fileName.substring(0, idx), fileName.substring(idx + 1) };
    } else {
        return new String[] { fileName, "" };
    }
}

private static void deleteFolder(File folder) {
    if (!folder.delete()) {
        LOG.warn("Was not able to delete folder {}", folder);
    }
}

