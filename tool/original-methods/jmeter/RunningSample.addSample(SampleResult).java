/**
 * Records a sample.
 *
 * @param res sample to record
 */
public void addSample(SampleResult res) {
    long aTimeInMillis = res.getTime();
    counter += res.getSampleCount();
    errorCount += res.getErrorCount();
    long startTime = res.getStartTime();
    long endTime = res.getEndTime();
    if (firstTime > startTime) {
        // this is our first sample, set the start time to current timestamp
        firstTime = startTime;
    }
    // Always update the end time
    if (lastTime < endTime) {
        lastTime = endTime;
    }
    runningSum += aTimeInMillis;
    if (aTimeInMillis > max) {
        max = aTimeInMillis;
    }
    if (aTimeInMillis < min) {
        min = aTimeInMillis;
    }
}