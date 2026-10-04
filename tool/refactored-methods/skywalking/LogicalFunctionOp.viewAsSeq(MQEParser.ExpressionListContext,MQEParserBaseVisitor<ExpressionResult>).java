private static ExpressionResult viewAsSeq(MQEParser.ExpressionListContext expressionListContext, MQEParserBaseVisitor<ExpressionResult> visitor) {
    ExpressionResult firstResult = null;
    for (MQEParser.ExpressionContext expContext : expressionListContext.expression()) {
        final ExpressionResult result = processExpression(expContext, visitor);
        if (firstResult == null) {
            firstResult = result;
        }
        if (result != null && !CollectionUtils.isEmpty(result.getResults()) && hasNonEmptyValue(result)) {
            return result;
        }
    }
    return firstResult;
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean hasNonEmptyValue(ExpressionResult result) {
    return result.getResults().stream().filter(s -> s != null && CollectionUtils.isNotEmpty(s.getValues())).flatMap(s -> s.getValues().stream()).anyMatch(s -> !s.isEmptyValue());
}

private static ExpressionResult processExpression(MQEParser.ExpressionContext expContext, MQEParserBaseVisitor<ExpressionResult> visitor) {
    return visitor.visit(expContext);
}

