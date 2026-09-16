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
    SimpleDateFormat dateFormat = new SimpleDateFormat(format != null ? format : "");
    return dateFormat.format(new Date(timeStamp));
}
// ---- helper method(s) introduced by the refactoring ----
private static long convertToHours(long milliseconds) {
    return milliseconds / 3600000;
}

private static long convertToMinutes(long milliseconds) {
    return milliseconds / 60000;
}

private static long convertToSeconds(long milliseconds) {
    return milliseconds / 1000;
}

private static String buildTimeString(long hours, long minutes, long seconds, long milliseconds) {
    if (hours > 0) {
        return hours + "h " + minutes + "m " + seconds + "s " + milliseconds + " ms";
    }
    if (minutes > 0) {
        return minutes + "m " + seconds + "s " + milliseconds + " ms";
    }
    if (seconds > 0) {
        return seconds + "s " + milliseconds + " ms";
    }
    if (milliseconds > 0) {
        return milliseconds + "ms";
    }
    return "0 ms";
}

