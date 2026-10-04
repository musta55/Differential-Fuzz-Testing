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
    String operationName = null;
    String query;
    String variables = null;
    final JsonNode operationNameNode = data.has(OPERATION_NAME_FIELD) ? data.get(OPERATION_NAME_FIELD) : null;
    if (operationNameNode != null) {
        operationName = getJsonNodeTextContent(operationNameNode, true);
    }
    if (!data.has(QUERY_FIELD)) {
        throw new IllegalArgumentException("Not a valid GraphQL query.");
    }
    final JsonNode queryNode = data.get(QUERY_FIELD);
    query = getJsonNodeTextContent(queryNode, false);
    final String trimmedQuery = StringUtils.trim(query);
    if (!StringUtils.startsWith(trimmedQuery, QUERY_FIELD) && !StringUtils.startsWith(trimmedQuery, "mutation")) {
        throw new IllegalArgumentException("Not a valid GraphQL query.");
    }
    final JsonNode variablesNode = data.has(VARIABLES_FIELD) ? data.get(VARIABLES_FIELD) : null;
    if (variablesNode != null) {
        final JsonNodeType nodeType = variablesNode.getNodeType();
        if (nodeType != JsonNodeType.NULL) {
            if (nodeType == JsonNodeType.OBJECT) {
                variables = OBJECT_MAPPER.writeValueAsString(variablesNode);
            } else {
                throw new IllegalArgumentException("Not a valid object node for GraphQL variables.");
            }
        }
    }
    return new GraphQLRequestParams(operationName, query, variables);
}