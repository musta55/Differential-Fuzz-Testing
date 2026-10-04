public void splitIntoNgrams(int minGram, int maxGram) {
    List<Token> ngramTokens = new ArrayList<>();
    for (int n = minGram; n <= maxGram; n++) {
        for (int i = 0; i < tokens.size() - n + 1; i++) {
            List<Token> subList = tokens.subList(i, i + n);
            Token token = new Token(subList);
            ngramTokens.add(token);
        }
    }
    tokens = ngramTokens;
}