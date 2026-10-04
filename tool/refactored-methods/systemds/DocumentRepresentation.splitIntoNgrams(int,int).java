public void splitIntoNgrams(int minGram, int maxGram) {
    List<Token> ngramTokens = createNgramTokens(minGram, maxGram);
    tokens = ngramTokens;
}
// ---- helper method(s) introduced by the refactoring ----
private List<Token> createNgramTokens(int minGram, int maxGram) {
    List<Token> ngramTokens = new ArrayList<>();
    for (int n = minGram; n <= maxGram; n++) {
        for (int i = 0; i < tokens.size() - n + 1; i++) {
            List<Token> subList = extractSubList(tokens, i, n);
            Token token = new Token(subList);
            ngramTokens.add(token);
        }
    }
    return ngramTokens;
}

private List<Token> extractSubList(List<Token> tokens, int start, int length) {
    return tokens.subList(start, start + length);
}

