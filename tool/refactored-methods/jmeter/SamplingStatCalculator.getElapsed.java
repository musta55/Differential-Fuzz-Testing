/**
 * Get the elapsed time for the samples
 *
 * @return how long the samples took
 */
public long getElapsed() {
    long endTime = getCurrentSample().getEndTime();
    return endTime == 0 ? 0 : endTime - firstTime;
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

