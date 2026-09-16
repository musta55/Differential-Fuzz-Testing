/**
 * Format the specified time stamp to string using the format.
 *
 * @param timeStamp
 *            the time stamp
 * @param format
 *            the format
 * @return the string
 */
@SuppressWarnings("JavaUtilDate")
public static String formatTimeStamp(long timeStamp, String format) {
    SimpleDateFormat dateFormat = format != null ? new SimpleDateFormat(format) : new SimpleDateFormat();
    return dateFormat.format(new Date(timeStamp));
}