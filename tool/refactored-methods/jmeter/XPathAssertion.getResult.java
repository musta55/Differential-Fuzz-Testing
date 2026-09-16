/**
 * Returns the result of the Assertion. Checks if the result is well-formed
 * XML, and that the XPath expression is matched (or not, as the case may
 * be)
 */
@Override
public AssertionResult getResult(SampleResult response) {
    AssertionResult result = new AssertionResult(getName());
    result.setFailure(false);
    result.setFailureMessage("");
    try {
        byte[] responseData = getResponseData(response);
        if (responseData == null || responseData.length == 0) {
            return result.setResultForNull();
        }
        if (log.isDebugEnabled()) {
            log.debug("Validation is set to {}, Whitespace is set to {}, Tolerant is set to {}", isValidating(), isWhitespace(), isTolerant());
        }
        Document doc = parseXML(responseData);
        if (doc == null || doc.getDocumentElement() == null) {
            result.setError(true);
            result.setFailureMessage("Document is null, probably not parsable");
            return result;
        }
        XPathUtil.computeAssertionResult(result, doc, getXPathString(), isNegated());
    } catch (Exception e) {
        handleParsingError(result, e.getClass().getSimpleName(), e);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private byte[] getResponseData(SampleResult response) {
    if (isScopeVariable()) {
        String inputString = getThreadContext().getVariables().get(getVariableName());
        return StringUtils.isEmpty(inputString) ? null : inputString.getBytes(StandardCharsets.UTF_8);
    } else {
        return response.getResponseData();
    }
}

private Document parseXML(byte[] responseData) throws SAXException, IOException, ParserConfigurationException, TidyException {
    boolean isXML = JOrphanUtils.isXML(responseData);
    return XPathUtil.makeDocument(new ByteArrayInputStream(responseData), isValidating(), isWhitespace(), isNamespace(), isTolerant(), isQuiet(), showWarnings(), reportErrors(), isXML, isDownloadDTDs());
}

private static void handleParsingError(AssertionResult result, String errorType, Exception e) {
    log.debug("Caught {} exception.", errorType, e);
    result.setError(true);
    result.setFailureMessage(errorType + ": " + e.getMessage());
}

