@Override
public String toString() {
    return "Samples: " + getCount() + "  " + "Avg: " + getMean() + "  " + "Min: " + getMin() + "  " + "Max: " + getMax() + "  " + "Error Rate: " + getErrorPercentage() + "  " + "Sample Rate: " + getRate();
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

