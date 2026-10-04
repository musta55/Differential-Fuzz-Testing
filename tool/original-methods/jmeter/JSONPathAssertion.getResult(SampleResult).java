@Override
public AssertionResult getResult(SampleResult samplerResult) {
    AssertionResult result = new AssertionResult(getName());
    String responseData = samplerResult.getResponseDataAsString();
    if (responseData.isEmpty()) {
        return result.setResultForNull();
    }
    result.setFailure(false);
    result.setFailureMessage("");
    if (!isInvert()) {
        try {
            doAssert(responseData);
        } catch (Exception e) {
            log.debug("Assertion failed", e);
            result.setFailure(true);
            result.setFailureMessage(e.getMessage());
        }
    } else {
        try {
            doAssert(responseData);
            result.setFailure(true);
            if (isJsonValidationBool()) {
                if (isExpectNull()) {
                    result.setFailureMessage("Failed that JSONPath " + getJsonPath() + " not matches null");
                } else {
                    result.setFailureMessage("Failed that JSONPath " + getJsonPath() + " not matches " + getExpectedValue());
                }
            } else {
                result.setFailureMessage("Failed that JSONPath not exists: " + getJsonPath());
            }
        } catch (Exception e) {
            log.debug("Assertion failed, as expected", e);
        }
    }
    return result;
}