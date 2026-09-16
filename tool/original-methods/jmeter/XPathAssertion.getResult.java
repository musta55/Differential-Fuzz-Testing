/**
 * Returns the result of the Assertion. Checks if the result is well-formed
 * XML, and that the XPath expression is matched (or not, as the case may
 * be)
 */
@Override
public AssertionResult getResult(SampleResult response) {
    // no error as default
    AssertionResult result = new AssertionResult(getName());
    result.setFailure(false);
    result.setFailureMessage("");
    byte[] responseData = null;
    Document doc = null;
    try {
        if (isScopeVariable()) {
            String inputString = getThreadContext().getVariables().get(getVariableName());
            if (!StringUtils.isEmpty(inputString)) {
                responseData = inputString.getBytes(StandardCharsets.UTF_8);
            }
        } else {
            responseData = response.getResponseData();
        }
        if (responseData == null || responseData.length == 0) {
            return result.setResultForNull();
        }
        if (log.isDebugEnabled()) {
            log.debug("Validation is set to {}, Whitespace is set to {}, Tolerant is set to {}", isValidating(), isWhitespace(), isTolerant());
        }
        boolean isXML = JOrphanUtils.isXML(responseData);
        doc = XPathUtil.makeDocument(new ByteArrayInputStream(responseData), isValidating(), isWhitespace(), isNamespace(), isTolerant(), isQuiet(), showWarnings(), reportErrors(), isXML, isDownloadDTDs());
    } catch (SAXException e) {
        log.debug("Caught sax exception.", e);
        result.setError(true);
        result.setFailureMessage("SAXException: " + e.getMessage());
        return result;
    } catch (IOException e) {
        log.warn("Cannot parse result content.", e);
        result.setError(true);
        result.setFailureMessage("IOException: " + e.getMessage());
        return result;
    } catch (ParserConfigurationException e) {
        log.warn("Cannot parse result content.", e);
        result.setError(true);
        result.setFailureMessage("ParserConfigurationException: " + e.getMessage());
        return result;
    } catch (TidyException e) {
        result.setError(true);
        result.setFailureMessage(e.getMessage());
        return result;
    }
    if (doc == null || doc.getDocumentElement() == null) {
        result.setError(true);
        result.setFailureMessage("Document is null, probably not parsable");
        return result;
    }
    XPathUtil.computeAssertionResult(result, doc, getXPathString(), isNegated());
    return result;
}