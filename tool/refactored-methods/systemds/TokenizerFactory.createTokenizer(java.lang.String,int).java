public static Tokenizer createTokenizer(String spec, int maxTokens) {
    Tokenizer tokenizer = null;
    try {
        //parse transform specification
        JSONObject jSpec = parseTransformSpecification(spec);
        // Algorithm to transform tokens into internal token representation
        TokenizerBuilder tokenizerBuilder = createTokenizerBuilder(jSpec);
        // Transform tokens to output representation
        TokenizerApplier tokenizerApplier = createTokenizerApplier(jSpec, maxTokens);
        tokenizer = new Tokenizer(tokenizerBuilder, tokenizerApplier);
    } catch (Exception ex) {
        throw new DMLRuntimeException(ex);
    }
    return tokenizer;
}
// ---- helper method(s) introduced by the refactoring ----
private static JSONObject parseTransformSpecification(String spec) throws Exception {
    JSONObject jSpec = new JSONObject(spec);
    // tokenization needs a text column to tokenize
    jSpec.getInt("tokenize_col");
    // tokenization needs one or more idCols that define the document and are replicated per token
    JSONArray idColsJsonArray = jSpec.getJSONArray("id_cols");
    int[] idCols = new int[idColsJsonArray.length()];
    for (int i = 0; i < idColsJsonArray.length(); i++) {
        idCols[i] = idColsJsonArray.getInt(i);
    }
    return jSpec;
}

private static TokenizerBuilder createTokenizerBuilder(JSONObject jSpec) throws Exception {
    String algo = jSpec.getString("algo");
    JSONObject algoParams = jSpec.has("algo_params") ? jSpec.getJSONObject("algo_params") : null;
    switch(algo) {
        case "split":
            return new TokenizerBuilderWhitespaceSplit(getIdColumns(jSpec), jSpec.getInt("tokenize_col"), algoParams);
        case "ngram":
            return new TokenizerBuilderNgram(getIdColumns(jSpec), jSpec.getInt("tokenize_col"), algoParams);
        default:
            throw new IllegalArgumentException("Algorithm {algo=" + algo + "} is not supported.");
    }
}

private static TokenizerApplier createTokenizerApplier(JSONObject jSpec, int maxTokens) throws Exception {
    String out = jSpec.getString("out");
    JSONObject outParams = jSpec.has("out_params") ? jSpec.getJSONObject("out_params") : null;
    boolean wideFormat = jSpec.has("format_wide") && jSpec.getBoolean("format_wide");
    boolean applyPadding = jSpec.has("apply_padding") && jSpec.getBoolean("apply_padding");
    int numIdCols = getIdColumns(jSpec).length;
    switch(out) {
        case "count":
            return new TokenizerApplierCount(numIdCols, maxTokens, wideFormat, applyPadding, outParams);
        case "position":
            return new TokenizerApplierPosition(numIdCols, maxTokens, wideFormat, applyPadding);
        case "hash":
            return new TokenizerApplierHash(numIdCols, maxTokens, wideFormat, applyPadding, outParams);
        default:
            throw new IllegalArgumentException("Output representation {out=" + out + "} is not supported.");
    }
}

private static int[] getIdColumns(JSONObject jSpec) throws Exception {
    JSONArray idColsJsonArray = jSpec.getJSONArray("id_cols");
    int[] idCols = new int[idColsJsonArray.length()];
    for (int i = 0; i < idColsJsonArray.length(); i++) {
        idCols[i] = idColsJsonArray.getInt(i);
    }
    return idCols;
}

