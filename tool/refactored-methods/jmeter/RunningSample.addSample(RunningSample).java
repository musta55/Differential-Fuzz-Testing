/**
 * Adds another RunningSample to this one.
 * Does not check if it has the same label and index.
 *
 * @param rs sample to add
 */
public void addSample(RunningSample rs) {
    this.counter += rs.counter;
    this.errorCount += rs.errorCount;
    this.runningSum += rs.runningSum;
    updateFirstAndLastTimes(rs);
    updateMinMax(rs);
}
// ---- helper method(s) introduced by the refactoring ----
private void updateCounters(SampleResult res) {
    counter += res.getSampleCount();
    errorCount += res.getErrorCount();
}

private void updateTimeStamps(SampleResult res) {
    long startTime = res.getStartTime();
    long endTime = res.getEndTime();
    if (firstTime > startTime) {
        firstTime = startTime;
    }
    if (lastTime < endTime) {
        lastTime = endTime;
    }
}

private void updateStatistics(SampleResult res) {
    long aTimeInMillis = res.getTime();
    runningSum += aTimeInMillis;
    if (aTimeInMillis > max) {
        max = aTimeInMillis;
    }
    if (aTimeInMillis < min) {
        min = aTimeInMillis;
    }
}

private void updateFirstAndLastTimes(RunningSample rs) {
    if (this.firstTime > rs.firstTime) {
        this.firstTime = rs.firstTime;
    }
    if (this.lastTime < rs.lastTime) {
        this.lastTime = rs.lastTime;
    }
}

private void updateMinMax(RunningSample rs) {
    if (this.max < rs.max) {
        this.max = rs.max;
    }
    if (this.min > rs.min) {
        this.min = rs.min;
    }
}

