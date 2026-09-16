/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.AbstractSummaryConsumer#updateData
     * (org.apache.jmeter.report.processor.AbstractSummaryConsumer.SummaryInfo,
     * org.apache.jmeter.report.core.Sample)
     */
@Override
protected void updateData(SummaryInfo info, Sample sample) {
    // Initialize overall data if they don't exist
    SummaryInfo overallInfo = getOverallInfo();
    Long overallData = overallInfo.getData();
    if (overallData == null) {
        overallData = ZERO;
    }
    overallInfo.setData(overallData + 1);
    // Process only failed samples
    if (!sample.getSuccess()) {
        errorCount++;
        Long data = info.getData();
        if (data == null) {
            data = ZERO;
        }
        info.setData(data + 1);
    }
}