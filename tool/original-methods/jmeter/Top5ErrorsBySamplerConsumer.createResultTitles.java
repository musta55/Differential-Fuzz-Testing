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
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_top5_label")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_top5_sample_count")));
    titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_top5_error_count")));
    for (int i = 0; i < MAX_NUMBER_OF_ERRORS_IN_TOP; i++) {
        titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_top5_error_label")));
        titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_top5_error_count")));
    }
    return titles;
}