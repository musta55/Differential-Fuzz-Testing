private static String getHeaderValueWithOroRegex(String multiPart, String regularExpression) {
    Perl5Matcher localMatcher = JMeterUtils.getMatcher();
    Pattern pattern = JMeterUtils.getPattern(regularExpression, Perl5Compiler.READ_ONLY_MASK | Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.MULTILINE_MASK);
    if (localMatcher.contains(multiPart, pattern)) {
        return localMatcher.getMatch().group(1).trim();
    }
    return null;
}