public static ExpressionResult doFunction0Op(ExpressionResult expResult, int opType) throws IllegalExpressionException {
    switch(opType) {
        case MQEParser.ABS:
            return absOperation(expResult);
        case MQEParser.CEIL:
            return ceilOperation(expResult);
        case MQEParser.FLOOR:
            return floorOperation(expResult);
    }
    throw new IllegalExpressionException("Unsupported function.");
}
// ---- helper method(s) introduced by the refactoring ----
private static ExpressionResult absOperation(ExpressionResult expResult) {
    return transResult(expResult, Math::abs);
}

private static ExpressionResult ceilOperation(ExpressionResult expResult) {
    return transResult(expResult, Math::ceil);
}

private static ExpressionResult floorOperation(ExpressionResult expResult) {
    return transResult(expResult, Math::floor);
}

private static ExpressionResult roundOperation(ExpressionResult expResult, int scale) {
    return transResult(expResult, aDouble -> {
        BigDecimal bd = BigDecimal.valueOf(aDouble);
        return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    });
}

