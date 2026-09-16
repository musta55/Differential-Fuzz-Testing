/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.csv.processor.SampleProcessor#setChannelAttribute
     * (int, java.lang.String, java.lang.Object)
     */
@Override
public void setChannelAttribute(int channel, String key, Object value) {
    getChannelContext(channel).put(key, value);
}