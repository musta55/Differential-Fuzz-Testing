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
    // Construct a multi-line string with all matches
    StringBuilder sb = new StringBuilder();
    final int size = matches.size();
    sb.append("Match count: ").append(size).append("\n");
    for (int j = 0; j < size; j++) {
        MatchResult mr = matches.get(j);
        final int groups = mr.groups();
        for (int i = 0; i < groups; i++) {
            sb.append("Match[").append(j + 1).append("][").append(i).append("]=").append(mr.group(i)).append("\n");
        }
    }
    return sb.toString();
}