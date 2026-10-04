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
        if (result.isArray()) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (JsonNode element : (ArrayNode) result) {
                builder.append("Result[").append(i++).append("]=").append(writeJsonNode(OBJECT_MAPPER, element)).append("\n");
            }
            return builder.toString();
        }
        return "Result[0]=" + writeJsonNode(OBJECT_MAPPER, result) + "\n";
    } catch (Exception e) {
        // NOSONAR We handle it through return message
        log.debug("Exception extracting from '{}' with expression '{}'", textToParse, expression);
        //$NON-NLS-1$
        return "Exception: " + e.getMessage();
    }
}