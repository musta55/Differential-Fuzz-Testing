/**
 * Records a sample.
 *
 * @param res the sample to record
 * @return newly created sample with current statistics
 */
public Sample addSample(SampleResult res) {
    long rtime;
    long cmean;
    long cstdv;
    long cmedian;
    long cpercent;
    long eCount;
    long endTime;
    double throughput;
    boolean rbool;
    synchronized (calculator) {
        calculator.addValue(res.getTime(), res.getSampleCount());
        calculator.addBytes(res.getBytesAsLong());
        calculator.addSentBytes(res.getSentBytes());
        setStartTime(res);
        eCount = getCurrentSample().getErrorCount() + res.getErrorCount();
        endTime = updateEndTime(res);
        long howLongRunning = endTime - firstTime;
        throughput = calculateThroughput(howLongRunning);
        updateMaxThroughput(throughput);
        rtime = res.getTime();
        cmean = (long) calculator.getMean();
        cstdv = (long) calculator.getStandardDeviation();
        cmedian = calculator.getMedian();
        cpercent = calculator.getPercentPoint(0.500);
        rbool = res.isSuccessful();
    }
    long count = calculator.getCount();
    Sample s = new Sample(null, rtime, cmean, cstdv, cmedian, cpercent, throughput, eCount, rbool, count, endTime);
    currentSample = s;
    return s;
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

