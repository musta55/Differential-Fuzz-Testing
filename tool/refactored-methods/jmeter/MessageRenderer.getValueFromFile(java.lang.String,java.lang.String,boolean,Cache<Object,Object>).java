/**
 * Read text from file, eventually replace variables, then convert it.
 * Cached content depends if variabilisation is active or not.
 *
 * @param filename
 *            name of the file to get the value from
 * @param encoding
 *            encoding of the file
 * @param hasVariable
 *            flag, whether variables inside the value should be replaced
 * @param cache
 *            Cache in which the raw values will be stored/read from
 * @return the constructed object
 */
default T getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    String fileContent = readFile(filename, encoding, cache);
    if (hasVariable) {
        fileContent = replaceVariables(fileContent);
    }
    return getValueFromText(fileContent);
}
// ---- helper method(s) introduced by the refactoring ----
default String readFile(String filename, String encoding, Cache<Object, Object> cache) {
    Object cachedValue = cache.getIfPresent(filename);
    if (cachedValue != null) {
        return (String) cachedValue;
    }
    try {
        String content = new String(Files.readAllBytes(Paths.get(filename)), Charset.forName(encoding));
        cache.put(filename, content);
        return content;
    } catch (IOException e) {
        throw new RuntimeException("Failed to read file: " + filename, e);
    }
}

default String replaceVariables(String content) {
    // Placeholder for variable replacement logic
    // Replace with actual variable replacement logic
    return content;
}

