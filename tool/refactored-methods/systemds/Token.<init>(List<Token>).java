public Token(List<Token> subList) {
    this(calculateTotalSubTokens(subList));
    for (Token token : subList) {
        subTokens.addAll(token.subTokens);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void addSubToken(String token, long startIndex) {
    subTokens.add(new SubToken(token, startIndex));
}

public static int calculateTotalSubTokens(List<Token> tokens) {
    int sum = 0;
    for (Token token : tokens) {
        sum += token.getNumSubTokens();
    }
    return sum;
}

