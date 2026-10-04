public Token(List<Token> subList) {
    this(getNumSubTokens(subList));
    for (Token token : subList) {
        subTokens.addAll(token.subTokens);
    }
}