public static String formatMillis(long millis) {
    long hours = extractHours(millis);
    long minutes = extractMinutes(millis);
    long seconds = extractSeconds(millis);
    long milliseconds = extractMilliseconds(millis);
    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
}
// ---- helper method(s) introduced by the refactoring ----
private static long extractHours(long millis) {
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    return hours;
}

private static long extractMinutes(long millis) {
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    return minutes;
}

private static long extractSeconds(long millis) {
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
    return seconds;
}

private static long extractMilliseconds(long millis) {
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
    millis -= TimeUnit.SECONDS.toMillis(seconds);
    return millis;
}

