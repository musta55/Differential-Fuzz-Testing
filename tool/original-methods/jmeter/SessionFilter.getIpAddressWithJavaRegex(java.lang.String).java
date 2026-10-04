private static String getIpAddressWithJavaRegex(String logLine) {
    Matcher matcher = IP_PATTERN.matcher(logLine);
    if (matcher.find()) {
        return matcher.group(0);
    }
    return "";
}