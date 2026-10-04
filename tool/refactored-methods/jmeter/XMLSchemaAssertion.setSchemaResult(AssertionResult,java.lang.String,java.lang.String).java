private static void setSchemaResult(AssertionResult result, String xmlStr, String xsdFileName) {
    try {
        DocumentBuilderFactory parserFactory = createDocumentBuilderFactory(xsdFileName);
        DocumentBuilder parser = parserFactory.newDocumentBuilder();
        parser.setErrorHandler(new SAXErrorHandler(result));
        parser.parse(new InputSource(new StringReader(xmlStr)));
        // if everything went fine then xml schema validation is valid
    } catch (SAXParseException e) {
        handleSAXParseException(result, e);
    } catch (SAXException | IOException | ParserConfigurationException e) {
        handleGeneralException(result, e);
    }
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

