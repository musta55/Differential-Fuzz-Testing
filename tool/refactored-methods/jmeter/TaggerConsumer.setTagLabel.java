public final void setTagLabel(String tagLabel) {
    if (tagLabel == null || tagLabel.isEmpty()) {
        throw new IllegalArgumentException("tagLabel cannot be null or empty");
    }
    this.tagLabel = tagLabel;
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

