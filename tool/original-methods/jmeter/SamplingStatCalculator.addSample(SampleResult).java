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
        eCount = getCurrentSample().getErrorCount();
        eCount += res.getErrorCount();
        endTime = getEndTime(res);
        long howLongRunning = endTime - firstTime;
        throughput = ((double) calculator.getCount() / (double) howLongRunning) * 1000.0;
        if (throughput > maxThroughput) {
            maxThroughput = throughput;
        }
        rtime = res.getTime();
        cmean = (long) calculator.getMean();
        cstdv = (long) calculator.getStandardDeviation();
        cmedian = calculator.getMedian();
        cpercent = calculator.getPercentPoint(0.500);
        // TODO cpercent is the same as cmedian here - why? and why pass it to "distributionLine"?
        rbool = res.isSuccessful();
    }
    long count = calculator.getCount();
    Sample s = new Sample(null, rtime, cmean, cstdv, cmedian, cpercent, throughput, eCount, rbool, count, endTime);
    currentSample = s;
    return s;
}