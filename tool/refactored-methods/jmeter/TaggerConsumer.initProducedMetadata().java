// Adds a new field in the sample metadata for each channel
private void initProducedMetadata() {
    clearBuilders();
    int channelCount = getConsumedChannelCount();
    for (int i = 0; i < channelCount; i++) {
        SampleMetadata consumedMetadata = getConsumedMetadata(i);
        SampleMetadata producedMetadata = createProducedMetadata(consumedMetadata);
        addSampleBuilder(producedMetadata, i);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void clearBuilders() {
    builders.clear();
}

private SampleMetadata createProducedMetadata(SampleMetadata consumedMetadata) {
    int colCount = consumedMetadata.getColumnCount();
    String[] names = new String[colCount + 1];
    for (int j = 0; j < colCount; j++) {
        names[j] = consumedMetadata.getColumnName(j);
    }
    names[colCount] = tagLabel;
    return new SampleMetadata(consumedMetadata.getSeparator(), names);
}

private void addSampleBuilder(SampleMetadata producedMetadata, int channel) {
    SampleBuilder builder = new SampleBuilder(producedMetadata);
    builders.add(builder);
    super.setProducedMetadata(producedMetadata, channel);
}

private void resetSampleIndexer() {
    if (sampleIndexer != null) {
        sampleIndexer.reset();
    }
}

