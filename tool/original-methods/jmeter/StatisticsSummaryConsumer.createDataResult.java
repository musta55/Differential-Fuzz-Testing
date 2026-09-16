/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.AbstractSummaryConsumer#createDataResult
     * (java.lang.String)
     */
@Override
protected ListResultData createDataResult(String key, StatisticsSummaryData data) {
    ListResultData result = new ListResultData();
    result.addResult(new ValueResultData(key != null ? key : JMeterUtils.getResString("reportgenerator_summary_total")));
    long total = data.getTotal();
    long errors = data.getErrors();
    result.addResult(new ValueResultData(total));
    result.addResult(new ValueResultData(errors));
    result.addResult(new ValueResultData((double) errors * 100 / total));
    result.addResult(new ValueResultData(data.getMean().getResult()));
    result.addResult(new ValueResultData(data.getMin()));
    result.addResult(new ValueResultData(data.getMax()));
    result.addResult(new ValueResultData(data.getMedian().getResult()));
    result.addResult(new ValueResultData(data.getPercentile1().getResult()));
    result.addResult(new ValueResultData(data.getPercentile2().getResult()));
    result.addResult(new ValueResultData(data.getPercentile3().getResult()));
    result.addResult(new ValueResultData(data.getThroughput()));
    result.addResult(new ValueResultData(data.getKBytesPerSecond()));
    result.addResult(new ValueResultData(data.getSentKBytesPerSecond()));
    return result;
}