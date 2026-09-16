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
    // Get the object to store counters or create it if it does not exist.
    SummaryInfo info = infos.get(key);
    if (info == null) {
        info = new SummaryInfo(supportsControllersDiscrimination && sample.isController());
        infos.put(key, info);
    }
    updateData(info, sample);
    super.produce(sample, channel);
}