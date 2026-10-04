/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.AbstractSummaryConsumer#createResultTitles
     * ()
     */
@Override
protected ListResultData createResultTitles() {
    ListResultData titles = new ListResultData();
    addTitle(titles, "reportgenerator_summary_statistics_label");
    addTitle(titles, "reportgenerator_summary_statistics_count");
    addTitle(titles, "reportgenerator_summary_statistics_error_count");
    addTitle(titles, "reportgenerator_summary_statistics_error_percent");
    addTitle(titles, "reportgenerator_summary_statistics_mean");
    addTitle(titles, "reportgenerator_summary_statistics_min");
    addTitle(titles, "reportgenerator_summary_statistics_max");
    addTitle(titles, "reportgenerator_summary_statistics_median");
    addPercentileTitles(titles);
    addTitle(titles, "reportgenerator_summary_statistics_throughput");
    addTitle(titles, "reportgenerator_summary_statistics_kbytes");
    addTitle(titles, "reportgenerator_summary_statistics_sent_kbytes");
    return titles;
}
// ---- helper method(s) introduced by the refactoring ----
private static void addValueToPercentilesAndStats(StatisticsSummaryData data, long elapsedTime) {
    data.getPercentile1().addValue((double) elapsedTime);
    data.getPercentile2().addValue((double) elapsedTime);
    data.getPercentile3().addValue((double) elapsedTime);
    data.getMean().addValue((double) elapsedTime);
    data.getMedian().addValue((double) elapsedTime);
}

private static void updateMinMaxAndTimes(StatisticsSummaryData data, Sample sample, long elapsedTime) {
    data.setMin(Math.min(data.getMin(), elapsedTime));
    data.setMax(Math.max(data.getMax(), elapsedTime));
    data.setFirstTime(Math.min(data.getFirstTime(), sample.getStartTime()));
    data.setEndTime(Math.max(data.getEndTime(), sample.getEndTime()));
}

private static StatisticsSummaryData getOrCreateData(SummaryInfo info, double percentileIndex1, double percentileIndex2, double percentileIndex3) {
    StatisticsSummaryData data = info.getData();
    if (data == null) {
        data = new StatisticsSummaryData(percentileIndex1, percentileIndex2, percentileIndex3);
        info.setData(data);
    }
    return data;
}

private static void addBasicStats(ListResultData result, StatisticsSummaryData data) {
    long total = data.getTotal();
    long errors = data.getErrors();
    result.addResult(new ValueResultData(total));
    result.addResult(new ValueResultData(errors));
    result.addResult(new ValueResultData(calculateErrorPercentage(total, errors)));
    result.addResult(new ValueResultData(data.getMean().getResult()));
    result.addResult(new ValueResultData(data.getMin()));
    result.addResult(new ValueResultData(data.getMax()));
    result.addResult(new ValueResultData(data.getMedian().getResult()));
}

private static double calculateErrorPercentage(long total, long errors) {
    return (double) errors * 100 / total;
}

private static void addPercentiles(ListResultData result, StatisticsSummaryData data) {
    result.addResult(new ValueResultData(data.getPercentile1().getResult()));
    result.addResult(new ValueResultData(data.getPercentile2().getResult()));
    result.addResult(new ValueResultData(data.getPercentile3().getResult()));
}

private static void addThroughputAndBytes(ListResultData result, StatisticsSummaryData data) {
    result.addResult(new ValueResultData(data.getThroughput()));
    result.addResult(new ValueResultData(data.getKBytesPerSecond()));
    result.addResult(new ValueResultData(data.getSentKBytesPerSecond()));
}

private static void addTitle(ListResultData titles, String resourceKey) {
    titles.addResult(new ValueResultData(JMeterUtils.getResString(resourceKey)));
}

private static void addPercentileTitles(ListResultData titles) {
    titles.addResult(new ValueResultData(formatPercentile(PCT1_LABEL)));
    titles.addResult(new ValueResultData(formatPercentile(PCT2_LABEL)));
    titles.addResult(new ValueResultData(formatPercentile(PCT3_LABEL)));
}

