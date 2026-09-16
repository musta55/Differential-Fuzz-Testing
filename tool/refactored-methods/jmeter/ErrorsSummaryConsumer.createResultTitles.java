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
    addTitle(titles, "reportgenerator_summary_errors_type");
    addTitle(titles, "reportgenerator_summary_errors_count");
    addTitle(titles, "reportgenerator_summary_errors_rate_error");
    addTitle(titles, "reportgenerator_summary_errors_rate_all");
    return titles;
}
// ---- helper method(s) introduced by the refactoring ----
private void addPercentageResults(ListResultData result, Long data) {
    result.addResult(new ValueResultData((double) data * 100 / errorCount));
    result.addResult(new ValueResultData((double) data * 100 / getOverallInfo().getData().doubleValue()));
}

private static String buildKey(String responseCode, String responseMessage) {
    return responseCode + (!StringUtils.isEmpty(responseMessage) ? "/" + escapeJson(responseMessage) : "");
}

private static boolean shouldUseAssertionFailedKey(String responseCode, Sample sample) {
    return MetricUtils.isSuccessCode(responseCode) || (StringUtils.isEmpty(responseCode) && StringUtils.isNotBlank(sample.getFailureMessage()));
}

private void initializeOverallData() {
    SummaryInfo overallInfo = getOverallInfo();
    Long overallData = overallInfo.getData();
    if (overallData == null) {
        overallData = ZERO;
    }
    overallInfo.setData(overallData + 1);
}

private void processFailedSample(SummaryInfo info, Sample sample) {
    if (!sample.getSuccess()) {
        errorCount++;
        Long data = info.getData();
        if (data == null) {
            data = ZERO;
        }
        info.setData(data + 1);
    }
}

private void resetState() {
    errorCount = 0L;
}

private static void addTitle(ListResultData titles, String resourceKey) {
    titles.addResult(new ValueResultData(JMeterUtils.getResString(resourceKey)));
}

