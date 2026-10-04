/**
 * Convert the GraphQL request parameters input data to an HTTP POST body string.
 * @param params GraphQL request parameter input data
 * @return an HTTP POST body string converted from the GraphQL request parameters input data
 * @throws RuntimeException if JSON serialization fails for some reason due to any runtime environment issues
 */
public static String toPostBodyString(final GraphQLRequestParams params) {
    final StringWriter writer = new StringWriter();
    try (JsonGenerator gen = jsonFactory.createGenerator(writer)) {
        gen.writeStartObject();
        writeOperationName(gen, params.getOperationName());
        writeVariables(gen, params.getVariables());
        writeQuery(gen, params.getQuery());
        gen.writeEndObject();
    } catch (IOException e) {
        throw new IllegalStateException("Error while writing graphql post body " + params, e);
    }
    return writer.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static void writeOperationName(JsonGenerator gen, String operationName) throws IOException {
    gen.writeStringField(OPERATION_NAME_FIELD, StringUtils.trimToNull(operationName));
}

private static void writeVariables(JsonGenerator gen, String variables) throws IOException {
    if (StringUtils.isNotBlank(variables)) {
        gen.writeFieldName(VARIABLES_FIELD);
        gen.writeRawValue(StringUtils.trim(variables));
    }
}

private static void writeQuery(JsonGenerator gen, String query) throws IOException {
    gen.writeStringField(QUERY_FIELD, StringUtils.trim(query));
}

private static String parseOperationName(ObjectNode data) {
    JsonNode operationNameNode = data.get(OPERATION_NAME_FIELD);
    return operationNameNode != null ? getJsonNodeTextContent(operationNameNode, true) : null;
}

private static String parseQuery(ObjectNode data) {
    JsonNode queryNode = data.get(QUERY_FIELD);
    if (queryNode == null) {
        throw new IllegalArgumentException("Not a valid GraphQL query.");
    }
    String query = getJsonNodeTextContent(queryNode, false);
    String trimmedQuery = StringUtils.trim(query);
    if (!StringUtils.startsWith(trimmedQuery, QUERY_FIELD) && !StringUtils.startsWith(trimmedQuery, "mutation")) {
        throw new IllegalArgumentException("Not a valid GraphQL query.");
    }
    return query;
}

private static String parseVariables(ObjectNode data) throws JsonProcessingException {
    JsonNode variablesNode = data.get(VARIABLES_FIELD);
    if (variablesNode == null || variablesNode.getNodeType() == JsonNodeType.NULL) {
        return null;
    }
    if (variablesNode.getNodeType() != JsonNodeType.OBJECT) {
        throw new IllegalArgumentException("Not a valid object node for GraphQL variables.");
    }
    return OBJECT_MAPPER.writeValueAsString(variablesNode);
}

private static void validateQuery(String query) {
    if (isNoQueryOrMutation(query)) {
        throw new IllegalArgumentException("Not a valid GraphQL query.");
    }
}

private static void validateVariables(String variables) {
    if (isNoJsonObject(variables)) {
        throw new IllegalArgumentException("Not a valid object node for GraphQL variables.");
    }
}

