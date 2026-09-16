/**
 * calculates the average page size, which means divide the bytes by number
 * of samples.
 *
 * @return average page size in bytes (0 if sample count is zero)
 */
public double getAvgPageBytes() {
    long count = calculator.getCount();
    if (count == 0) {
        return 0;
    }
    return calculator.getTotalBytes() / (double) count;
}