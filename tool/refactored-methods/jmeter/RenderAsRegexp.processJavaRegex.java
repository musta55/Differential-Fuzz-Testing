private String processJavaRegex(String textToParse) {
    java.util.regex.Pattern pattern;
    try {
        pattern = JMeterUtils.compilePattern(regexpField.getText());
    } catch (PatternSyntaxException e) {
        return e.toString();
    }
    Matcher matcher = pattern.matcher(textToParse);
    List<java.util.regex.MatchResult> matches = new ArrayList<>();
    while (matcher.find()) {
        matches.add(matcher.toMatchResult());
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

