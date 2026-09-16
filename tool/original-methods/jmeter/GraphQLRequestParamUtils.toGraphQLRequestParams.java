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
    if (isNoQueryOrMutation(query)) {
        throw new IllegalArgumentException("Not a valid GraphQL query.");
    }
    if (isNoJsonObject(variables)) {
        throw new IllegalArgumentException("Not a valid object node for GraphQL variables.");
    }
    return new GraphQLRequestParams(operationName, query, variables);
}