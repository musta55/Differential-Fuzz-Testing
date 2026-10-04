public List<Token> splitIntoNgrams(Token token, int minGram, int maxGram) {
    validateToken(token);
    String tokenText = token.toString();
    List<Token> newTokens = new ArrayList<>();
    for (int n = minGram; n <= maxGram; n++) {
        for (int i = 0; i < tokenText.length() - n + 1; i++) {
            String substring = tokenText.substring(i, i + n);
            newTokens.add(new Token(substring, token.getStartIndex(0) + i));
        }
    }
    return newTokens;
}
// ---- helper method(s) introduced by the refactoring ----
private void setNgramType(String type) {
    if (type.equals("document")) {
        this.ngramType = NgramType.DOCUMENT;
    } else if (type.equals("token")) {
        this.ngramType = NgramType.TOKEN;
    } else {
        throw new DMLRuntimeException("Invalid ngram type, choose between 'token' and 'document'");
    }
}

private void validateToken(Token token) {
    if (token.getNumSubTokens() == 0)
        throw new DMLRuntimeException("Cannot create ngram of token where there are no subTokens");
    if (token.getNumSubTokens() != 1)
        throw new DMLRuntimeException("Cannot create ngram of token where there are more than 1 subTokens");
}

