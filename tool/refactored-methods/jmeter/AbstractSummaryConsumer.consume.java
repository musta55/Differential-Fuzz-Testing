/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.SampleConsumer#consume(org.apache.
     * jmeter.report.core.Sample, int)
     */
@Override
public void consume(Sample sample, int channel) {
    String key = getKeyFromSample(sample);
    SummaryInfo info = infos.computeIfAbsent(key, k -> new SummaryInfo(supportsControllersDiscrimination && sample.isController()));
    updateData(info, sample);
    super.produce(sample, channel);
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

