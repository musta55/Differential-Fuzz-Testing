/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.AbstractSummaryConsumer#updateData
     * (org.apache.jmeter.report.processor.AbstractSummaryConsumer.SummaryInfo,
     * org.apache.jmeter.report.core.Sample)
     */
@Override
protected void updateData(SummaryInfo info, Sample sample) {
    if (sample.isEmptyController()) {
        return;
    }
    // Initialize overall data if they don't exist
    SummaryInfo overallInfo = getOverallInfo();
    ApdexSummaryData overallData = overallInfo.getData();
    if (overallData == null) {
        overallData = new ApdexSummaryData(getThresholdSelector().select(null));
        overallInfo.setData(overallData);
    }
    // Initialize info data if they don't exist
    ApdexSummaryData data = info.getData();
    if (data == null) {
        data = new ApdexSummaryData(getThresholdSelector().select(sample.getName()));
        info.setData(data);
    }
    // Increment the total count of samples with the current name
    data.incTotalCount();
    // Increment the total count of samples
    overallData.incTotalCount();
    // Process only succeeded samples
    if (sample.getSuccess()) {
        long elapsedTime = sample.getElapsedTime();
        // Increment the counters depending on the elapsed time.
        ApdexThresholdsInfo thresholdsInfo = data.getApdexThresholdInfo();
        if (elapsedTime <= thresholdsInfo.getSatisfiedThreshold()) {
            data.incSatisfiedCount();
        } else if (elapsedTime <= thresholdsInfo.getToleratedThreshold()) {
            data.incToleratedCount();
        }
        // Increment the overall counters depending on the elapsed time.
        ApdexThresholdsInfo overallThresholdsInfo = overallData.getApdexThresholdInfo();
        if (elapsedTime <= overallThresholdsInfo.getSatisfiedThreshold()) {
            overallData.incSatisfiedCount();
        } else if (elapsedTime <= overallThresholdsInfo.getToleratedThreshold()) {
            overallData.incToleratedCount();
        }
    }
}