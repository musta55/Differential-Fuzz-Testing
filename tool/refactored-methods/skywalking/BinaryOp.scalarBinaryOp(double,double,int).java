//scalar with scalar
private static double scalarBinaryOp(double leftValue, double rightValue, int opType) {
    BiFunction<Double, Double, Double> operation = SCALAR_OPERATIONS.get(opType);
    if (operation == null) {
        throw new IllegalArgumentException("Unsupported operation type: " + opType);
    }
    return operation.apply(leftValue, rightValue);
}