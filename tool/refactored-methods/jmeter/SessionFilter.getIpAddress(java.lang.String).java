protected static String getIpAddress(String logLine) {
    return USE_JAVA_REGEX ? getIpAddressWithJavaRegex(logLine) : getIpAddressWithOroRegex(logLine);
}