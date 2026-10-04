public static ExpressionResult doFunction0Op(ExpressionResult expResult, int opType) throws IllegalExpressionException {
    switch(opType) {
        case MQEParser.ABS:
            return MathematicalFunctionOp.transResult(expResult, Math::abs);
        case MQEParser.CEIL:
            return MathematicalFunctionOp.transResult(expResult, Math::ceil);
        case MQEParser.FLOOR:
            return MathematicalFunctionOp.transResult(expResult, Math::floor);
    }
    throw new IllegalExpressionException("Unsupported function.");
}