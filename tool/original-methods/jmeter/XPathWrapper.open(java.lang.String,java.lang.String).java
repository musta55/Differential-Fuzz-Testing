private static XPathFileContainer open(String file, String xpathString) {
    if (log.isInfoEnabled()) {
        log.info("{}: Opening {}", Thread.currentThread().getName(), file);
    }
    XPathFileContainer frcc = null;
    try {
        frcc = new XPathFileContainer(file, xpathString);
    } catch (TransformerException | SAXException | ParserConfigurationException | IOException e) {
        log.warn(e.getLocalizedMessage());
    }
    return frcc;
}