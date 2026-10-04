private static String showEOL(final String input) {
    StringBuilder sb = new StringBuilder(input.length() * 2);
    for (int i = 0; i < input.length(); i++) {
        char ch = input.charAt(i);
        if (ch < ' ') {
            sb.append('[').append((int) ch).append(']');
        } else {
            sb.append(ch);
        }
    }
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeEolByte() {
    setEolByte(EOL_INT);
    if (useEolByte) {
        log.info("Using eolByte={}", eolByte);
    }
}

private void initializeCharset() {
    setCharset(CHARSET);
    String configuredCharset = JMeterUtils.getProperty("tcp.charset");
    if (StringUtils.isEmpty(configuredCharset)) {
        log.info("Using platform default charset:{}", CHARSET);
    } else {
        log.info("Using charset:{}", configuredCharset);
    }
}

