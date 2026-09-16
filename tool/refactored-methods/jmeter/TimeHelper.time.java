public static String time(long t) {
    long hours = convertToHours(t);
    t %= 3600000;
    long minutes = convertToMinutes(t);
    t %= 60000;
    long seconds = convertToSeconds(t);
    t %= 1000;
    long milliseconds = t;
    return buildTimeString(hours, minutes, seconds, milliseconds);
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

