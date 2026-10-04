/**
 * Record timestamp bucket format in Downsampling Unit.
 *
 * @param timestamp    Timestamp
 * @param downsampling Downsampling
 * @return timestamp in downsampling format
 */
public static long getTimeBucket(long timestamp, DownSampling downsampling) {
    TimeBucketConverter converter = CONVERTERS.get(downsampling);
    if (converter == null) {
        throw new UnexpectedException("Unknown downsampling value.");
    }
    return converter.toTimeBucket(timestamp);
}