/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.processor.SampleConsumer#startConsuming()
     */
@Override
public void startConsuming() {
    // Broadcast metadata to consumes for each channel
    int channelCount = getConsumedChannelCount();
    for (int i = 0; i < channelCount; i++) {
        super.setProducedMetadata(getConsumedMetadata(i), i);
    }
    super.startProducing();
}