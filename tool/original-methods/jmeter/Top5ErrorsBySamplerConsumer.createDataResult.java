/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.AbstractSummaryConsumer#createDataResult
     * (java.lang.String)
     */
@Override
protected ListResultData createDataResult(String key, Top5ErrorsSummaryData data) {
    ListResultData result = new ListResultData();
    long errors = data.getErrors();
    if (errors > 0 || key == null) {
        result.addResult(new ValueResultData(key != null ? key : JMeterUtils.getResString("reportgenerator_top5_total")));
        long total = data.getTotal();
        result.addResult(new ValueResultData(total));
        result.addResult(new ValueResultData(errors));
        Object[][] top5 = data.getTop5ErrorsMetrics();
        int numberOfValues = 0;
        for (int i = 0; i < top5.length; i++) {
            result.addResult(new ValueResultData(top5[i][0]));
            result.addResult(new ValueResultData(top5[i][1]));
            numberOfValues++;
        }
        for (int i = numberOfValues; i < MAX_NUMBER_OF_ERRORS_IN_TOP; i++) {
            result.addResult(new ValueResultData(""));
            result.addResult(new ValueResultData(""));
        }
    }
    return result;
}