/**
 * calculates the average page size, which means divide the bytes by number
 * of samples.
 *
 * @return average page size in bytes (0 if sample count is zero)
 */
public double getAvgPageBytes() {
    long count = calculator.getCount();
    return count == 0 ? 0 : calculator.getTotalBytes() / (double) count;
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

