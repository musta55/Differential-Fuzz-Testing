public static String time(long t) {
    long h = t / 3600000;
    t %= 3600000;
    long m = t / 60000;
    t %= 60000;
    long s = t / 1000;
    t %= 1000;
    long ms = t;
    if (h > 0) {
        return h + "h " + m + "m " + s + "s " + ms + " ms";
    }
    if (m > 0) {
        return m + "m " + s + "s " + ms + " ms";
    }
    if (s > 0) {
        return s + "s " + ms + " ms";
    }
    if (ms > 0) {
        return ms + "ms";
    }
    return "0 ms";
}