@Override
public Serializable getValueFromFile(String filename, String encoding, boolean hasVariable, Cache<Object, Object> cache) {
    Serializable value;
    if (hasVariable) {
        value = getInterpretedContent(filename, encoding, hasVariable, cache);
    } else {
        value = (Serializable) cache.get(filename, p -> retrieveContentFromFile(filename));
    }
    return value;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Try to determine encoding based on XML prolog, if none <code>null</code> is returned. *
 */
protected String determineEncoding(String pEncoding, String filename) {
    if (!PublisherSampler.DEFAULT_ENCODING.equals(pEncoding)) {
        return pEncoding;
    }
    return findEncoding(filename);
}

protected Serializable retrieveContentFromFile(String filename) {
    return (Serializable) JMeterUtils.createXStream().fromXML(new File(filename));
}

