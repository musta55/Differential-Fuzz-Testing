/**
 * Record timestamp bucket format in Downsampling Unit.
 *
 * @param timestamp    Timestamp
 * @param downsampling Downsampling
 * @return timestamp in downsampling format
 */
public static long getTimeBucket(long timestamp, DownSampling downsampling) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timestamp);
    long year = calendar.get(Calendar.YEAR);
    long month = calendar.get(Calendar.MONTH) + 1;
    long day = calendar.get(Calendar.DAY_OF_MONTH);
    long hour = calendar.get(Calendar.HOUR_OF_DAY);
    long minute = calendar.get(Calendar.MINUTE);
    long second = calendar.get(Calendar.SECOND);
    switch(downsampling) {
        case Second:
            return year * 10000000000L + month * 100000000 + day * 1000000 + hour * 10000 + minute * 100 + second;
        case Minute:
            return year * 100000000 + month * 1000000 + day * 10000 + hour * 100 + minute;
        case Hour:
            return year * 1000000 + month * 10000 + day * 100 + hour;
        case Day:
            return year * 10000 + month * 100 + day;
        default:
            throw new UnexpectedException("Unknown downsampling value.");
    }
}