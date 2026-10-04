private static String getIpAddressWithJavaRegex(String logLine) {
    Matcher matcher = IP_PATTERN.matcher(logLine);
    return matcher.find() ? matcher.group(0) : "";
}