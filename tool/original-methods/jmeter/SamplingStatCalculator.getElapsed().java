/**
 * Get the elapsed time for the samples
 *
 * @return how long the samples took
 */
public long getElapsed() {
    if (getCurrentSample().getEndTime() == 0) {
        // No samples collected ...
        return 0;
    }
    return getCurrentSample().getEndTime() - firstTime;
}