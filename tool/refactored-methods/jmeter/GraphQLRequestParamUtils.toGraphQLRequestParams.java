/**
 * Parse {@code arguments} and convert it to a {@link GraphQLRequestParams} object if it has valid GraphQL HTTP arguments.
 * @param arguments arguments
 * @param contentEncoding content encoding
 * @return a converted {@link GraphQLRequestParams} object form the {@code arguments}
 * @throws IllegalArgumentException if {@code arguments} does not contain valid GraphQL request arguments
 * @throws UnsupportedEncodingException if it fails to decode parameter value
 */
public static GraphQLRequestParams toGraphQLRequestParams(final Arguments arguments, final String contentEncoding) throws UnsupportedEncodingException {
    final String encoding = StringUtils.defaultIfEmpty(contentEncoding, EncoderCache.URL_ARGUMENT_ENCODING);
    String operationName = null;
    String query = null;
    String variables = null;
    for (JMeterProperty prop : arguments) {
        final Argument arg = (Argument) prop.getObjectValue();
        if (!(arg instanceof HTTPArgument)) {
            continue;
        }
        final String name = arg.getName();
        final String metadata = arg.getMetaData();
        final String value = StringUtils.trimToNull(arg.getValue());
        if ("=".equals(metadata) && value != null) {
            final boolean alwaysEncoded = ((HTTPArgument) arg).isAlwaysEncoded();
            if (OPERATION_NAME_FIELD.equals(name)) {
                operationName = encodedField(value, encoding, alwaysEncoded);
            } else if (QUERY_FIELD.equals(name)) {
                query = encodedField(value, encoding, alwaysEncoded);
            } else if (VARIABLES_FIELD.equals(name)) {
                variables = encodedField(value, encoding, alwaysEncoded);
            }
        }
    }
    validateQuery(query);
    validateVariables(variables);
    return new GraphQLRequestParams(operationName, query, variables);
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

