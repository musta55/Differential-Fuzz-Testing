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
    LinkExtractorParser parser = getCachedParser(parserClassName);
    if (parser != null) {
        return parser;
    }
    parser = createNewParser(parserClassName);
    if (parser.isReusable()) {
        cacheParser(parserClassName, parser);
    }
    return parser;
}
// ---- helper method(s) introduced by the refactoring ----
private static LinkExtractorParser getCachedParser(String parserClassName) {
    LinkExtractorParser parser = PARSERS.get(parserClassName);
    if (parser != null) {
        LOG.debug("Fetched {}", parserClassName);
    }
    return parser;
}

private static LinkExtractorParser createNewParser(String parserClassName) throws LinkExtractorParseException {
    try {
        Object clazz = Class.forName(parserClassName).getDeclaredConstructor().newInstance();
        if (clazz instanceof LinkExtractorParser) {
            LinkExtractorParser parser = (LinkExtractorParser) clazz;
            LOG.info("Created {}", parserClassName);
            return parser;
        } else {
            throw new LinkExtractorParseException(new ClassCastException(parserClassName));
        }
    } catch (IllegalArgumentException | ReflectiveOperationException | SecurityException e) {
        throw new LinkExtractorParseException(e);
    }
}

private static void cacheParser(String parserClassName, LinkExtractorParser parser) {
    LinkExtractorParser currentParser = PARSERS.putIfAbsent(parserClassName, parser);
    if (currentParser != null) {
        LOG.debug("Parser already cached for {}", parserClassName);
    }
}

