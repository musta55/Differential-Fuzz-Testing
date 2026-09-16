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
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_errors_type")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_errors_count")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_errors_rate_error")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_errors_rate_all")));
    return titles;
}