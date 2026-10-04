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
        doAssert(result, responseData, isInvert());
    } catch (Exception e) {
        if (!isInvert()) {
            result.setError(true);
            result.setFailureMessage(e.getMessage());
        }
    }
    return result;
}