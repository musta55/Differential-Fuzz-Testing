/**
 * Parse {@code postData} and convert it to a {@link GraphQLRequestParams} object if it is a valid GraphQL post data.
 * @param postData post data
 * @param contentEncoding content encoding
 * @return a converted {@link GraphQLRequestParams} object form the {@code postData}
 * @throws IllegalArgumentException if {@code postData} is not a GraphQL post JSON data or not a valid JSON
 * @throws JsonProcessingException if it fails to serialize a parsed JSON object to string
 * @throws UnsupportedEncodingException if it fails to decode parameter value
 */
public static GraphQLRequestParams toGraphQLRequestParams(byte[] postData, final String contentEncoding) throws JsonProcessingException, UnsupportedEncodingException {
    final String encoding = StringUtils.isNotEmpty(contentEncoding) ? contentEncoding : EncoderCache.URL_ARGUMENT_ENCODING;
    ObjectNode data;
    try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(postData), encoding)) {
        data = OBJECT_MAPPER.readValue(reader, ObjectNode.class);
    } catch (IOException e) {
        throw new IllegalArgumentException("Invalid json data: " + e.getLocalizedMessage(), e);
    }
    String operationName = parseOperationName(data);
    String query = parseQuery(data);
    String variables = parseVariables(data);
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

