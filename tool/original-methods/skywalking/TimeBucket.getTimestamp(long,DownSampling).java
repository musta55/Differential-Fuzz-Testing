/**
 * Convert TimeBucket to Timestamp in millisecond.
 *
 * @param timeBucket   long
 * @param downsampling Downsampling
 * @return timestamp in millisecond unit
 */
public static long getTimestamp(long timeBucket, DownSampling downsampling) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    switch(downsampling) {
        case Second:
            calendar.set(Calendar.SECOND, (int) (timeBucket % 100));
            timeBucket /= 100;
        // Fall through
        case Minute:
            calendar.set(Calendar.MINUTE, (int) (timeBucket % 100));
            timeBucket /= 100;
        // Fall through
        case Hour:
            calendar.set(Calendar.HOUR_OF_DAY, (int) (timeBucket % 100));
            timeBucket /= 100;
        // Fall through
        case Day:
            calendar.set(Calendar.DAY_OF_MONTH, (int) (timeBucket % 100));
            timeBucket /= 100;
            calendar.set(Calendar.MONTH, (int) (timeBucket % 100) - 1);
            calendar.set(Calendar.YEAR, (int) (timeBucket / 100));
            break;
        default:
            throw new UnexpectedException("Unknown downsampling value.");
    }
    return calendar.getTimeInMillis();
}