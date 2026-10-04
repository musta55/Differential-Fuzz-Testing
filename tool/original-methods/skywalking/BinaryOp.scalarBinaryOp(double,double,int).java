//scalar with scalar
private static double scalarBinaryOp(double leftValue, double rightValue, int opType) {
    double calculatedResult = 0;
    switch(opType) {
        case MQEParser.ADD:
            calculatedResult = leftValue + rightValue;
            break;
        case MQEParser.SUB:
            calculatedResult = leftValue - rightValue;
            break;
        case MQEParser.MUL:
            calculatedResult = leftValue * rightValue;
            break;
        case MQEParser.DIV:
            calculatedResult = leftValue / rightValue;
            break;
        case MQEParser.MOD:
            calculatedResult = leftValue % rightValue;
            break;
    }
    return calculatedResult;
}