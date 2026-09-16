/**
 * @param value long
 * @return rate per second
 */
private double getRatePerSecond(long value) {
    double rate = 0;
    long elapsed = getElapsed();
    if (elapsed > 0 && value > 0) {
        rate = value / ((double) elapsed / 1000);
    }
    return Math.max(rate, 0);
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

