/**
 * <p>Gets content with variable replaced.</p>
 * <p>If pEncoding {@link PublisherSampler#DEFAULT_ENCODING isn't provided}, try to find it.</p>
 * <p>Only raw text is cached, neither interpreted text, neither parsed object.</p>
 */
protected Serializable getInterpretedContent(String filename, final String pEncoding, boolean hasVariable, Cache<Object, Object> cache) {
    String encoding = determineEncoding(pEncoding, filename);
    String stringValue = delegate.getValueFromFile(filename, encoding, hasVariable, cache);
    return (Serializable) JMeterUtils.createXStream().fromXML(stringValue);
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

