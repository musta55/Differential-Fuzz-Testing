/**
 * Returns the result of the Assertion. Checks if the result is well-formed XML,
 * and that the XPath expression is matched (or not, as the case may be)
 */
@Override
public AssertionResult getResult(SampleResult response) {
    // no error as default
    AssertionResult result = new AssertionResult(getName());
    result.setFailure(false);
    result.setFailureMessage("");
    String responseData = getResponseData(response);
    if (responseData == null) {
        return result.setResultForNull();
    }
    try {
        XPathUtil.computeAssertionResultUsingSaxon(result, responseData, getXPathString(), getNamespaces(), isNegated());
    } catch (CompletionException | SaxonApiException e) {
        // NOSONAR We handle exception within result failure message
        result.setError(true);
        // CompletionException happens if caching fails
        result.setFailureMessage("Exception occurred computing assertion with XPath:" + getXPathString() + ", error:" + e.getMessage());
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private String getResponseData(SampleResult response) {
    String responseData = null;
    if (isScopeVariable()) {
        String inputString = getThreadContext().getVariables().get(getVariableName());
        if (!StringUtils.isEmpty(inputString)) {
            responseData = inputString;
        }
    } else {
        responseData = response.getResponseDataAsString();
    }
    return responseData;
}

