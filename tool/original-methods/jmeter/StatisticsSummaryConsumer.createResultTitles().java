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
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_label")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_count")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_error_count")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_error_percent")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_mean")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_min")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_max")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_median")));
    titles.addResult(new ValueResultData(formatPercentile(PCT1_LABEL)));
    titles.addResult(new ValueResultData(formatPercentile(PCT2_LABEL)));
    titles.addResult(new ValueResultData(formatPercentile(PCT3_LABEL)));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_throughput")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_kbytes")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_summary_statistics_sent_kbytes")));
    return titles;
}