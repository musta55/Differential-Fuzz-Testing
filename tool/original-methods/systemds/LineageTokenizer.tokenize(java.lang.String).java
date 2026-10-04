public Map<String, String> tokenize(String str) {
    Map<String, String> tokens = new HashMap<>();
    for (TokenInfo info : _tokenInfos) {
        Matcher m = info.regex.matcher(str);
        if (m.find()) {
            String tok = m.group().trim();
            if (!info.key.isEmpty())
                tokens.put(info.key, tok);
            str = m.replaceFirst("");
        }
    }
    if (!str.isEmpty())
        throw new ParseException("Unexpected character in input: " + str);
    return tokens;
}