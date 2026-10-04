public Map<String, String> tokenize(String str) {
    Map<String, String> tokens = new HashMap<>();
    for (TokenInfo info : _tokenInfos) {
        str = processToken(str, info, tokens);
    }
    if (!str.isEmpty()) {
        throw new ParseException("Unexpected character in input: " + str);
    }
    return tokens;
}
// ---- helper method(s) introduced by the refactoring ----
private String processToken(String str, TokenInfo info, Map<String, String> tokens) {
    Matcher matcher = info.regex.matcher(str);
    if (matcher.find()) {
        String token = matcher.group().trim();
        if (!info.key.isEmpty()) {
            tokens.put(info.key, token);
        }
        str = matcher.replaceFirst("");
    }
    return str;
}

