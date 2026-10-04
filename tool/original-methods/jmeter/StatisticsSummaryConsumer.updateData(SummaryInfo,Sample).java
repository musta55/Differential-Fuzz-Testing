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
    SummaryInfo overallInfo = getOverallInfo();
    StatisticsSummaryData overallData = overallInfo.getData();
    if (overallData == null) {
        overallData = new StatisticsSummaryData(PERCENTILE_INDEX1, PERCENTILE_INDEX2, PERCENTILE_INDEX3);
        overallInfo.setData(overallData);
    }
    StatisticsSummaryData data = info.getData();
    if (data == null) {
        data = new StatisticsSummaryData(PERCENTILE_INDEX1, PERCENTILE_INDEX2, PERCENTILE_INDEX3);
        info.setData(data);
    }
    if (!sample.isEmptyController()) {
        aggregateSample(sample, data, false);
        aggregateSample(sample, overallData, true);
    }
}