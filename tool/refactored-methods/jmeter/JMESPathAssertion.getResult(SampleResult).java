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
        doAssert(result, responseData);
    } catch (Exception e) {
        if (!isInvert()) {
            result.setError(true);
            result.setFailureMessage(e.getMessage());
        }
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Used to do a JMESPath query and compute result if the expectedValue matches
 * with the JMESPath query result
 *
 * @param assertionResult          {@link AssertionResult}
 * @param responseDataAsJsonString the response data from the sender
 * @throws Exception
 */
private void doAssert(AssertionResult assertionResult, String responseDataAsJsonString) throws Exception {
    boolean invert = isInvert();
    // cast the response data to JsonNode
    JsonNode input = OBJECT_MAPPER.readValue(responseDataAsJsonString, JsonNode.class);
    // get the JMESPath expression from the cache
    // if it does not exist, compile it.
    // Expression does not compile if JMESPath expression is empty or null
    Expression<JsonNode> expression = JMESPathCache.getInstance().get(getJmesPath());
    // get the result from the JMESPath query
    JsonNode currentValue = expression.search(input);
    log.debug("JMESPath query {} invoked on response {}. Query result is {}. ", expression, responseDataAsJsonString, currentValue);
    boolean success = checkResult(OBJECT_MAPPER, currentValue);
    if (success != invert) {
        failAssertion(assertionResult);
    }
}

private String buildFailureMessage() {
    StringBuilder message = new StringBuilder();
    if (!isJsonValidationBool()) {
        message.append("JMESPATH ").append(getJmesPath()).append(" expected");
        addNegation(message);
        message.append(" to exist");
    } else {
        message.append("Value expected");
        if (isExpectNull()) {
            addNegation(message);
            message.append(" to be null");
        } else {
            if (isUseRegex()) {
                addNegation(message);
                message.append(" to match ");
                message.append(getExpectedValue());
            } else {
                addNegation(message);
                message.append(" to be equal to ");
                message.append(getExpectedValue());
            }
        }
    }
    return message.toString();
}

private void addNegation(StringBuilder message) {
    if (isInvert()) {
        message.append(" not");
    }
}

private void failAssertion(AssertionResult assertionResult) {
    assertionResult.setFailure(true);
    assertionResult.setFailureMessage(buildFailureMessage());
}

