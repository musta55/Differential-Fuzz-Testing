protected static String getIpAddress(String logLine) {
    if (USE_JAVA_REGEX) {
        return getIpAddressWithJavaRegex(logLine);
    }
    return getIpAddressWithOroRegex(logLine);
}