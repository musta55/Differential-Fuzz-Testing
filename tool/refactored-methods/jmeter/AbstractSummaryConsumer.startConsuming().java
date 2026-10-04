/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.processor.SampleConsumer#startConsuming()
     */
@Override
public void startConsuming() {
    for (int i = 0; i < getConsumedChannelCount(); i++) {
        super.setProducedMetadata(getConsumedMetadata(i), i);
    }
    super.startProducing();
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Indicates whether this summary can discriminate controller samples
 *
 * @return true, if this summary can discriminate controller samples; false
 *         otherwise.
 */
public final boolean supportsControllersDiscrimination() {
    return supportsControllersDiscrimination;
}

