public Token(List<String> tokens, List<Long> startIndex) {
    this(tokens.size());
    if (tokens.size() != startIndex.size())
        throw new DMLRuntimeException("Cannot create token from mismatched input sizes");
    for (int i = 0; i < tokens.size(); i++) {
        addSubToken(tokens.get(i), startIndex.get(i));
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

