public Token(List<String> tokens, List<Long> startIndex) {
    this(tokens.size());
    if (tokens.size() != startIndex.size())
        throw new DMLRuntimeException("Cannot create token from mismatched input sizes");
    for (int i = 0; i < tokens.size(); i++) {
        subTokens.add(new SubToken(tokens.get(i), startIndex.get(i)));
    }
}