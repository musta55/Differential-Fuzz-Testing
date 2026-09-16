/**
 * Factory method of parsers. Instances might get cached, when
 * {@link LinkExtractorParser#isReusable()} on the newly created instance
 * equals {@code true}.
 *
 * @param parserClassName
 *            name of the class that should be used to create new parsers
 * @return a possibly cached instance of the wanted
 *         {@link LinkExtractorParser}
 * @throws LinkExtractorParseException
 *             when a new instance could not be instantiated
 */
public static LinkExtractorParser getParser(String parserClassName) throws LinkExtractorParseException {
    // Is there a cached parser?
    LinkExtractorParser parser = PARSERS.get(parserClassName);
    if (parser != null) {
        LOG.debug("Fetched {}", parserClassName);
        return parser;
    }
    try {
        Object clazz = Class.forName(parserClassName).getDeclaredConstructor().newInstance();
        if (clazz instanceof LinkExtractorParser) {
            parser = (LinkExtractorParser) clazz;
        } else {
            throw new LinkExtractorParseException(new ClassCastException(parserClassName));
        }
    } catch (IllegalArgumentException | ReflectiveOperationException | SecurityException e) {
        throw new LinkExtractorParseException(e);
    }
    LOG.info("Created {}", parserClassName);
    if (parser.isReusable()) {
        LinkExtractorParser currentParser = PARSERS.putIfAbsent(parserClassName, // cache the parser if not already
        parser);
        // done by another thread
        if (currentParser != null) {
            return currentParser;
        }
    }
    return parser;
}