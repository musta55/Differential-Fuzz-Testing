/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.processor.SampleConsumer#stopConsuming()
     */
@Override
public void stopConsuming() {
    super.stopConsuming();
    // Reset state
    errorCount = 0L;
}