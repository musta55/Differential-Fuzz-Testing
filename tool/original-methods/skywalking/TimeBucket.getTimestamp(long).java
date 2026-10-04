/**
 * Convert TimeBucket to Timestamp in millisecond.
 *
 * @param timeBucket long
 * @return timestamp in millisecond unit
 */
public static long getTimestamp(long timeBucket) {
    if (isSecondBucket(timeBucket)) {
        return getTimestamp(timeBucket, DownSampling.Second);
    } else if (isMinuteBucket(timeBucket)) {
        return getTimestamp(timeBucket, DownSampling.Minute);
    } else if (isHourBucket(timeBucket)) {
        return getTimestamp(timeBucket, DownSampling.Hour);
    } else if (isDayBucket(timeBucket)) {
        return getTimestamp(timeBucket, DownSampling.Day);
    } else {
        throw new UnexpectedException("Unknown downsampling value.");
    }
}