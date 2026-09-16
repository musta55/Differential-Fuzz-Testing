@Override
public byte[] getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    if (hasVariable) {
        return getBytesFromString(filename, encoding, hasVariable, cache);
    } else {
        return getBytesFromCacheOrFile(filename, cache);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private byte[] getBytesFromString(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    String stringValue = delegate.getValueFromFile(filename, encoding, hasVariable, cache);
    try {
        return stringValue.getBytes(encoding);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Unable to read " + filename, e);
    }
}

private byte[] getBytesFromCacheOrFile(String filename, Cache<Object, Object> cache) {
    return (byte[]) cache.get(filename, key -> getContent(filename));
}

