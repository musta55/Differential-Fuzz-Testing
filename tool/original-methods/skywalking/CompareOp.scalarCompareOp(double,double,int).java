private static int scalarCompareOp(double leftValue, double rightValue, int opType) {
    int comparedResult = 0;
    switch(opType) {
        case MQEParser.DEQ:
            comparedResult = boolToInt(leftValue == rightValue);
            break;
        case MQEParser.NEQ:
            comparedResult = boolToInt(leftValue != rightValue);
            break;
        case MQEParser.GT:
            comparedResult = boolToInt(leftValue > rightValue);
            break;
        case MQEParser.LT:
            comparedResult = boolToInt(leftValue < rightValue);
            break;
        case MQEParser.GTE:
            comparedResult = boolToInt(leftValue >= rightValue);
            break;
        case MQEParser.LTE:
            comparedResult = boolToInt(leftValue <= rightValue);
            break;
    }
    return comparedResult;
}