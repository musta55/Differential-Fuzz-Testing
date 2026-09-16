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
        addTop5ErrorsToResult(result, data.getTop5ErrorsMetrics());
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Update the data based upon information from the sample.
 *
 * @param sample {@link Sample}
 * @param data {@link Top5ErrorsSummaryData}
 * @param isOverall boolean indicating if aggregation concerns the overall results
 *                  in which case we ignore Transaction Controller's SampleResult
 */
private static void aggregateSample(Sample sample, Top5ErrorsSummaryData data, boolean isOverall, boolean ignoreTCFromTop5ErrorsBySampler) {
    if (sample.isController()) {
        if (isOverall || ignoreTCFromTop5ErrorsBySampler) {
            return;
        }
    }
    if (!sample.getSuccess()) {
        data.registerError(ErrorsSummaryConsumer.getErrorKey(sample));
        data.incErrors();
    }
    data.incTotal();
}

private static void addTop5ErrorsToResult(ListResultData result, Object[][] top5) {
    int numberOfValues = 0;
    for (int i = 0; i < top5.length; i++) {
        result.addResult(new ValueResultData(top5[i][0]));
        result.addResult(new ValueResultData(top5[i][1]));
        numberOfValues++;
    }
    addEmptyResultsForMissingTop5Errors(result, numberOfValues);
}

private static void addEmptyResultsForMissingTop5Errors(ListResultData result, int numberOfValues) {
    for (int i = numberOfValues; i < MAX_NUMBER_OF_ERRORS_IN_TOP; i++) {
        result.addResult(new ValueResultData(""));
        result.addResult(new ValueResultData(""));
    }
}

private static void addTop5ErrorTitles(ListResultData titles) {
    for (int i = 0; i < MAX_NUMBER_OF_ERRORS_IN_TOP; i++) {
        titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_top5_error_label")));
        titles.addResult(new ValueResultData(JMeterUtils.getResString("reportgenerator_top5_error_count")));
    }
}

