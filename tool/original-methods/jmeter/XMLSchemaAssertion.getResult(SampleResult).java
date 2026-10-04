@Override
public AssertionResult getResult(SampleResult response) {
    AssertionResult result = new AssertionResult(getName());
    // Note: initialised with error = failure = false
    String resultData = response.getResponseDataAsString();
    if (resultData.length() == 0) {
        return result.setResultForNull();
    }
    String xsdFileName = getXsdFileName();
    log.debug("xmlString: {}, xsdFileName: {}", resultData, xsdFileName);
    if (xsdFileName == null || xsdFileName.length() == 0) {
        result.setResultForFailure(FILE_NAME_IS_REQUIRED);
    } else {
        setSchemaResult(result, resultData, xsdFileName);
    }
    return result;
}