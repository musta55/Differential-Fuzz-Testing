/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.csv.processor.GraphValueSelector#select(java
     * .lang.String, java.lang.Object, org.apache.jmeter.report.csv.core.Sample)
     */
@Override
public Double select(String series, Sample sample) {
    if (isIgnoreTransactionController()) {
        if (!sample.isController()) {
            return (double) sample.getLatency();
        }
    } else {
        if (!sample.isEmptyController()) {
            return (double) sample.getLatency();
        }
    }
    return null;
}