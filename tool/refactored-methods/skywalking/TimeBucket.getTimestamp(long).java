/**
 * Convert TimeBucket to Timestamp in millisecond.
 *
 * @param timeBucket long
 * @return timestamp in millisecond unit
 */
public static long getTimestamp(long timeBucket) {
    for (Map.Entry<DownSampling, TimeBucketConverter> entry : CONVERTERS.entrySet()) {
        if (entry.getValue().isValid(timeBucket)) {
            return entry.getValue().toTimestamp(timeBucket);
        }
    }
    throw new UnexpectedException("Unknown downsampling value.");
}