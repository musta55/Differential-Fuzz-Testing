private static String getIpAddressWithOroRegex(String logLine) {
    Pattern pattern = JMeterUtils.getPatternCache().getPattern("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", Perl5Compiler.READ_ONLY_MASK | Perl5Compiler.SINGLELINE_MASK);
    Perl5Matcher matcher = JMeterUtils.getMatcher();
    return matcher.contains(logLine, pattern) ? matcher.getMatch().group(0) : "";
}