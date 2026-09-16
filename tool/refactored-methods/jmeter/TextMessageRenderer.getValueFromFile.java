@Override
public String getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    String text = fetchContentFromCache(filename, encoding, cache);
    if (hasVariable) {
        text = resolveVariables(text);
    }
    return text;
}
// ---- helper method(s) introduced by the refactoring ----
private static String fetchContentFromCache(String filename, String encoding, Cache<Object, Object> cache) {
    return (String) cache.get(new FileKey(filename, encoding), key -> fetchContent((FileKey) key));
}

private static String fetchContent(FileKey key) {
    return new TextFile(key.getFilename(), key.getEncoding()).getText();
}

private static String resolveVariables(String text) {
    return new CompoundVariable(text).execute();
}

