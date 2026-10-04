/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.csv.processor.GraphValueSelector#select(java
     * .lang.String, java.lang.Object, org.apache.jmeter.report.csv.core.Sample)
     */
@Override
public Double select(String series, Sample sample) {
    if (!isIgnoreTransactionController() || !sample.isController()) {
        if (!sample.isEmptyController()) {
            return (double) sample.getConnectTime();
        }
    }
    return null;
}