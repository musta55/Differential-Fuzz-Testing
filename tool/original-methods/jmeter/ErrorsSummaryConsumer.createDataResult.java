/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.AbstractSummaryConsumer#createDataResult
     * (java.lang.String)
     */
@Override
protected ListResultData createDataResult(String key, Long data) {
    ListResultData result = new ListResultData();
    result.addResult(new ValueResultData(key != null ? key : JMeterUtils.getResString("reportgenerator_summary_total")));
    result.addResult(new ValueResultData(data));
    result.addResult(new ValueResultData((double) data * 100 / errorCount));
    result.addResult(new ValueResultData((double) data * 100 / getOverallInfo().getData().doubleValue()));
    return result;
}