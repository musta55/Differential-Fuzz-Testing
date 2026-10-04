@Override
public Serializable getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    Serializable value;
    if (hasVariable) {
        value = getInterpretedContent(filename, encoding, hasVariable, cache);
    } else {
        value = (Serializable) cache.get(filename, p -> getContent(filename));
    }
    return value;
}