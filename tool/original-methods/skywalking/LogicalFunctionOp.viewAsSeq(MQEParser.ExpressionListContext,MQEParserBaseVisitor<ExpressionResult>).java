private static ExpressionResult viewAsSeq(MQEParser.ExpressionListContext expressionListContext, MQEParserBaseVisitor<ExpressionResult> visitor) {
    ExpressionResult firstResult = null;
    for (MQEParser.ExpressionContext expContext : expressionListContext.expression()) {
        final ExpressionResult result = visitor.visit(expContext);
        if (firstResult == null) {
            firstResult = result;
        }
        if (result == null || CollectionUtils.isEmpty(result.getResults())) {
            continue;
        }
        final boolean isNotEmptyValue = result.getResults().stream().filter(s -> s != null && CollectionUtils.isNotEmpty(s.getValues())).flatMap(s -> s.getValues().stream()).anyMatch(s -> !s.isEmptyValue());
        if (isNotEmptyValue) {
            return result;
        }
    }
    return firstResult;
}