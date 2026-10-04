public Token(String token, long startIndex) {
    this(1);
    subTokens.add(new SubToken(token, startIndex));
}