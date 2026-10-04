public static ExpressionResult doFunction1Op(ExpressionResult expResult, int opType, int scale) throws IllegalExpressionException {
    switch(opType) {
        case MQEParser.ROUND:
            return MathematicalFunctionOp.transResult(expResult, aDouble -> {
                BigDecimal bd = BigDecimal.valueOf(aDouble);
                return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
            });
    }
    throw new IllegalExpressionException("Unsupported function.");
}