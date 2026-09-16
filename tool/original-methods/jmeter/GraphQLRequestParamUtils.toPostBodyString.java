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
        gen.writeStringField(OPERATION_NAME_FIELD, StringUtils.trimToNull(params.getOperationName()));
        if (StringUtils.isNotBlank(params.getVariables())) {
            gen.writeFieldName(VARIABLES_FIELD);
            gen.writeRawValue(StringUtils.trim(params.getVariables()));
        }
        gen.writeStringField(QUERY_FIELD, StringUtils.trim(params.getQuery()));
        gen.writeEndObject();
    } catch (IOException e) {
        throw new IllegalStateException("Error while writing graphql post body " + params, e);
    }
    return writer.toString();
}