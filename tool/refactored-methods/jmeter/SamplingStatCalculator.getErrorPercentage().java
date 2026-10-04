/**
 * Returns the raw double value of the percentage of samples with errors
 * that were recorded. (Between 0.0 and 1.0)
 *
 * @return the raw double value of the percentage of samples with errors
 *         that were recorded.
 */
public double getErrorPercentage() {
    if (calculator.getCount() == 0) {
        return 0.0;
    }
    return (double) getCurrentSample().getErrorCount() / calculator.getCount();
}
// ---- helper method(s) introduced by the refactoring ----
private long updateEndTime(SampleResult res) {
    long endTime = res.getEndTime();
    long lastTime = getCurrentSample().getEndTime();
    return Math.max(lastTime, endTime);
}

private double calculateThroughput(long howLongRunning) {
    return calculator.getCount() / (double) howLongRunning * 1000.0;
}

private void updateMaxThroughput(double throughput) {
    if (throughput > maxThroughput) {
        maxThroughput = throughput;
    }
}

