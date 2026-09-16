public SampleMetadata parse(String headRow) {
    char useSep = guessDelimiter(headRow);
    String[] cols = headRow.split(Pattern.quote(Character.toString(useSep)));
    return new SampleMetadata(useSep, cols);
}
// ---- helper method(s) introduced by the refactoring ----
private char guessDelimiter(String headRow) {
    char useSep = separator;
    if (headRow.indexOf(useSep) < 0 && !ALL_WORD_CHARS.matcher(headRow).matches()) {
        Matcher matcher = DELIMITER_PATTERN.matcher(headRow);
        if (matcher.matches()) {
            String guessedSep = matcher.group(2);
            if (guessedSep.length() != 1) {
                throw new IllegalArgumentException("We guessed a delimiter of '" + guessedSep + "', but we support only one-character-separators");
            }
            useSep = guessedSep.charAt(0);
            logger.warn("Use guessed delimiter '{}' instead of configured '{}'. " + "Please configure the property 'jmeter.save.saveservice.default_delimiter={}'", useSep, separator, useSep);
        }
    }
    return useSep;
}

