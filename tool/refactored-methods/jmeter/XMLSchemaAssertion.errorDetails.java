// Helper method to construct SAX error details
private static String errorDetails(SAXParseException spe) {
    StringBuilder str = new StringBuilder(80);
    int lineNumber = spe.getLineNumber();
    if (lineNumber != -1) {
        str.append("line=").append(lineNumber).append(" col=").append(spe.getColumnNumber()).append(" ");
    }
    str.append(spe.getLocalizedMessage());
    return str.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static DocumentBuilderFactory createDocumentBuilderFactory(String xsdFileName) throws ParserConfigurationException {
    DocumentBuilderFactory parserFactory = DocumentBuilderFactory.newInstance();
    parserFactory.setValidating(true);
    parserFactory.setNamespaceAware(true);
    parserFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
    parserFactory.setAttribute(JAXP_SCHEMA_SOURCE, xsdFileName);
    parserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    return parserFactory;
}

private static void handleSAXParseException(AssertionResult result, SAXParseException e) {
    if (!result.isError() && !result.isFailure()) {
        result.setError(true);
        result.setFailureMessage(errorDetails(e));
    }
}

private static void handleGeneralException(AssertionResult result, Exception e) {
    if (log.isWarnEnabled()) {
        log.warn(e.toString(), e);
    }
    result.setResultForFailure(e.getMessage());
}

