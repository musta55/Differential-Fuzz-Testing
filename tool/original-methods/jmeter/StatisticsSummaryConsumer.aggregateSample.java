/**
 * @param sample {@link Sample}
 * @param data {@link StatisticsSummaryData}
 * @param isOverall boolean indicating if aggregation concerns the Overall results in which case we ignore Transaction Controller's SampleResult
 */
private static void aggregateSample(Sample sample, StatisticsSummaryData data, boolean isOverall) {
    if (isOverall && sample.isController()) {
        return;
    }
    data.incTotal();
    data.incBytes(sample.getReceivedBytes());
    data.incSentBytes(sample.getSentBytes());
    if (!sample.getSuccess()) {
        data.incErrors();
    }
    long elapsedTime = sample.getElapsedTime();
    data.getPercentile1().addValue((double) elapsedTime);
    data.getPercentile2().addValue((double) elapsedTime);
    data.getPercentile3().addValue((double) elapsedTime);
    data.getMean().addValue((double) elapsedTime);
    data.getMedian().addValue((double) elapsedTime);
    data.setMin(elapsedTime);
    data.setMax(elapsedTime);
    data.setFirstTime(sample.getStartTime());
    data.setEndTime(sample.getEndTime());
}