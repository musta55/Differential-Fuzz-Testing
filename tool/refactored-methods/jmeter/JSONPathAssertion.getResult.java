@Override
public AssertionResult getResult(SampleResult samplerResult) {
    AssertionResult result = new AssertionResult(getName());
    String responseData = samplerResult.getResponseDataAsString();
    if (responseData.isEmpty()) {
        return result.setResultForNull();
    }
    result.setFailure(false);
    result.setFailureMessage("");
    try {
        doAssert(responseData);
        if (isInvert()) {
            result.setFailure(true);
            result.setFailureMessage(buildInvertedFailureMessage());
        }
    } catch (Exception e) {
        log.debug(isInvert() ? "Assertion failed, as expected" : "Assertion failed", e);
        if (!isInvert()) {
            result.setFailure(true);
            result.setFailureMessage(e.getMessage());
        }
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleNonValidation(Object value) {
    if (value instanceof JSONArray) {
        JSONArray arrayValue = (JSONArray) value;
        if (arrayValue.isEmpty() && !JsonPath.isPathDefinite(getJsonPath())) {
            throw new IllegalStateException(String.format("JSONPath '%s' is indefinite and the extracted Value is an empty Array." + " Please use an assertion value, to be sure to get a correct result. Expected value was '%s'", getJsonPath(), getExpectedValue()));
        }
    }
}

private void handleAssertionFailure(Object value) {
    if (isExpectNull()) {
        throw new IllegalStateException(String.format("Value in json path '%s' expected to be null, but found '%s'", getJsonPath(), value));
    } else {
        String msg = isUseRegex() ? "Value in json path '%s' expected to match regexp '%s', but it did not match: '%s'" : "Value in json path '%s' expected to be '%s', but found '%s'";
        throw new IllegalStateException(String.format(msg, getJsonPath(), getExpectedValue(), objectToString(value)));
    }
}

private String buildInvertedFailureMessage() {
    if (isJsonValidationBool()) {
        return isExpectNull() ? "Failed that JSONPath " + getJsonPath() + " not matches null" : "Failed that JSONPath " + getJsonPath() + " not matches " + getExpectedValue();
    } else {
        return "Failed that JSONPath not exists: " + getJsonPath();
    }
}

