/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.csv.processor.SampleProcessor#getChannelAttribute
     * (int, java.lang.String)
     */
@Override
public Object getChannelAttribute(int channel, String key) {
    return getChannelContext(channel).get(key);
}