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
    // Construct a multi-line string with all matches
    StringBuilder sb = new StringBuilder();
    final int size = matches.size();
    sb.append("Match count: ").append(size).append("\n");
    for (int j = 0; j < size; j++) {
        java.util.regex.MatchResult mr = matches.get(j);
        final int groups = mr.groupCount();
        for (int i = 0; i <= groups; i++) {
            sb.append("Match[").append(j + 1).append("][").append(i).append("]=").append(mr.group(i)).append("\n");
        }
    }
    return sb.toString();
}