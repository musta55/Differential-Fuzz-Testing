// Adds a new field in the sample metadata for each channel
private void initProducedMetadata() {
    builders.clear();
    int channelCount = getConsumedChannelCount();
    for (int i = 0; i < channelCount; i++) {
        // Get the metadata for the current channel
        SampleMetadata consumedMetadata = getConsumedMetadata(i);
        // Copy metadata to an array
        int colCount = consumedMetadata.getColumnCount();
        String[] names = new String[colCount + 1];
        for (int j = 0; j < colCount; j++) {
            names[j] = consumedMetadata.getColumnName(j);
        }
        // Add the new field
        names[colCount] = tagLabel;
        // Build the produced metadata from the array
        SampleMetadata producedMetadata = new SampleMetadata(consumedMetadata.getSeparator(), names);
        // Add a sample builder for the current channel
        builders.add(new SampleBuilder(producedMetadata));
        super.setProducedMetadata(producedMetadata, i);
    }
}