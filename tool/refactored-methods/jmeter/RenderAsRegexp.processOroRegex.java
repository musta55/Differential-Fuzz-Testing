private String processOroRegex(String textToParse) {
    Perl5Matcher matcher = new Perl5Matcher();
    PatternMatcherInput input = new PatternMatcherInput(textToParse);
    PatternCacheLRU pcLRU = new PatternCacheLRU();
    Pattern pattern;
    try {
        pattern = pcLRU.getPattern(regexpField.getText(), Perl5Compiler.READ_ONLY_MASK);
    } catch (MalformedCachePatternException e) {
        return e.toString();
    }
    List<MatchResult> matches = new ArrayList<>();
    while (matcher.contains(input, pattern)) {
        matches.add(matcher.getMatch());
    }
    return formatMatches(matches);
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatMatches(List<?> matches) {
    StringBuilder sb = new StringBuilder();
    final int size = matches.size();
    sb.append("Match count: ").append(size).append("\n");
    for (int j = 0; j < size; j++) {
        Object mr = matches.get(j);
        int groups;
        if (mr instanceof java.util.regex.MatchResult) {
            groups = ((java.util.regex.MatchResult) mr).groupCount();
            for (int i = 0; i <= groups; i++) {
                sb.append("Match[").append(j + 1).append("][").append(i).append("]=").append(((java.util.regex.MatchResult) mr).group(i)).append("\n");
            }
        } else if (mr instanceof MatchResult) {
            groups = ((MatchResult) mr).groups();
            for (int i = 0; i < groups; i++) {
                sb.append("Match[").append(j + 1).append("][").append(i).append("]=").append(((MatchResult) mr).group(i)).append("\n");
            }
        }
    }
    return sb.toString();
}

