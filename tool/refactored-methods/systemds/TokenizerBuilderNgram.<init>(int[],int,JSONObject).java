public TokenizerBuilderNgram(int[] idCols, int tokenizeCol, JSONObject params) throws JSONException {
    super(idCols, tokenizeCol, params);
    if (params != null && params.has("min_gram")) {
        this.minGram = params.getInt("min_gram");
    }
    if (params != null && params.has("max_gram")) {
        this.maxGram = params.getInt("max_gram");
    }
    if (params != null && params.has("ngram_type")) {
        String type = params.getString("ngram_type").toLowerCase();
        setNgramType(type);
    }
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

