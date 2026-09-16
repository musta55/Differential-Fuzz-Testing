@Override
public byte[] getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    byte[] bytes;
    if (hasVariable) {
        String stringValue = delegate.getValueFromFile(filename, encoding, hasVariable, cache);
        try {
            bytes = stringValue.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to read " + filename, e);
        }
    } else {
        bytes = (byte[]) cache.get(filename, _p -> getContent(filename));
    }
    return bytes;
}