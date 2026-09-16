/**
 * <p>Gets content with variable replaced.</p>
 * <p>If pEncoding {@link PublisherSampler#DEFAULT_ENCODING isn't provided}, try to find it.</p>
 * <p>Only raw text is cached, neither interpreted text, neither parsed object.</p>
 */
protected Serializable getInterpretedContent(String filename, final String pEncoding, boolean hasVariable, Cache<Object, Object> cache) {
    String encoding = pEncoding;
    if (PublisherSampler.DEFAULT_ENCODING.equals(encoding)) {
        encoding = findEncoding(filename);
    }
    String stringValue = delegate.getValueFromFile(filename, encoding, hasVariable, cache);
    return (Serializable) JMeterUtils.createXStream().fromXML(stringValue);
}