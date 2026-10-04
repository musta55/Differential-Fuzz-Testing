private static int scalarCompareOp(double leftValue, double rightValue, int opType) {
    switch(opType) {
        case MQEParser.DEQ:
            return compareEqual(leftValue, rightValue);
        case MQEParser.NEQ:
            return compareNotEqual(leftValue, rightValue);
        case MQEParser.GT:
            return compareGreaterThan(leftValue, rightValue);
        case MQEParser.LT:
            return compareLessThan(leftValue, rightValue);
        case MQEParser.GTE:
            return compareGreaterThanOrEqual(leftValue, rightValue);
        case MQEParser.LTE:
            return compareLessThanOrEqual(leftValue, rightValue);
        default:
            throw new IllegalArgumentException("Unsupported operation type");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static int compareEqual(double leftValue, double rightValue) {
    return boolToInt(leftValue == rightValue);
}

private static int compareNotEqual(double leftValue, double rightValue) {
    return boolToInt(leftValue != rightValue);
}

private static int compareGreaterThan(double leftValue, double rightValue) {
    return boolToInt(leftValue > rightValue);
}

private static int compareLessThan(double leftValue, double rightValue) {
    return boolToInt(leftValue < rightValue);
}

private static int compareGreaterThanOrEqual(double leftValue, double rightValue) {
    return boolToInt(leftValue >= rightValue);
}

private static int compareLessThanOrEqual(double leftValue, double rightValue) {
    return boolToInt(leftValue <= rightValue);
}

