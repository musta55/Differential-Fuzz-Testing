/**
 * @param value long
 * @return rate per second
 */
private double getRatePerSecond(long value) {
    double rate = 0;
    if (this.getElapsed() > 0 && value > 0) {
        rate = value / ((double) this.getElapsed() / 1000);
    }
    if (rate < 0) {
        rate = 0;
    }
    return rate;
}