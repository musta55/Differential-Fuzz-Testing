public static String formatMillis(long millis) {
    long m = millis;
    long hours = TimeUnit.MILLISECONDS.toHours(m);
    m -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(m);
    m -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(m);
    m -= TimeUnit.SECONDS.toMillis(seconds);
    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, m);
}