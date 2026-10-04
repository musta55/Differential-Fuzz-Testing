/**
 * Convert TimeBucket to Timestamp in millisecond.
 *
 * @param timeBucket   long
 * @param downsampling Downsampling
 * @return timestamp in millisecond unit
 */
public static long getTimestamp(long timeBucket, DownSampling downsampling) {
    TimeBucketConverter converter = CONVERTERS.get(downsampling);
    if (converter == null) {
        throw new UnexpectedException("Unknown downsampling value.");
    }
    return converter.toTimestamp(timeBucket);
}