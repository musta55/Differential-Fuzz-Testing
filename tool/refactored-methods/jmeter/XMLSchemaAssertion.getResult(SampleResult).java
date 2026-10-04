@Override
public AssertionResult getResult(SampleResult response) {
    AssertionResult result = new AssertionResult(getName());
    // Note: initialised with error = failure = false
    String resultData = response.getResponseDataAsString();
    if (resultData.isEmpty()) {
        return result.setResultForNull();
    }
    String xsdFileName = getXsdFileName();
    log.debug("xmlString: {}, xsdFileName: {}", resultData, xsdFileName);
    if (xsdFileName == null || xsdFileName.isEmpty()) {
        result.setResultForFailure(FILE_NAME_IS_REQUIRED);
    } else {
        setSchemaResult(result, resultData, xsdFileName);
    }
    return result;
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

