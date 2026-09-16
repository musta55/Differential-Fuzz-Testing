@Override
protected String process(String textToParse) {
    String expression = getExpression();
    try {
        JsonNode actualObj = OBJECT_MAPPER.readValue(textToParse, JsonNode.class);
        JsonNode result = JMESPathCache.getInstance().get(expression).search(actualObj);
        if (result.isNull()) {
            //$NON-NLS-1$
            return NO_MATCH;
        }
        return formatResult(result);
    } catch (Exception e) {
        // NOSONAR We handle it through return message
        log.debug("Exception extracting from '{}' with expression '{}'", textToParse, expression);
        //$NON-NLS-1$
        return "Exception: " + e.getMessage();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatResult(JsonNode result) throws JsonProcessingException {
    if (result.isArray()) {
        return formatArrayResult((ArrayNode) result);
    }
    return "Result[0]=" + writeJsonNode(OBJECT_MAPPER, result) + "\n";
}

private static String formatArrayResult(ArrayNode arrayNode) throws JsonProcessingException {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < arrayNode.size(); i++) {
        JsonNode element = arrayNode.get(i);
        builder.append("Result[").append(i).append("]=").append(writeJsonNode(OBJECT_MAPPER, element)).append("\n");
    }
    return builder.toString();
}

