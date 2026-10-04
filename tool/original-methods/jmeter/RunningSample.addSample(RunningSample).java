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
    if (this.firstTime > rs.firstTime) {
        this.firstTime = rs.firstTime;
    }
    if (this.lastTime < rs.lastTime) {
        this.lastTime = rs.lastTime;
    }
    if (this.max < rs.max) {
        this.max = rs.max;
    }
    if (this.min > rs.min) {
        this.min = rs.min;
    }
}