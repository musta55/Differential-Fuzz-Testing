private static ExpressionResult calculateRate(ExpressionResult expResult, int trendRange, Step step) {
    ExpressionResult result = calculateIncrease(expResult, trendRange);
    long rangeSeconds;
    switch(step) {
        case SECOND:
            rangeSeconds = trendRange;
            break;
        case MINUTE:
            rangeSeconds = trendRange * 60;
            break;
        case HOUR:
            rangeSeconds = trendRange * 3600;
            break;
        case DAY:
            rangeSeconds = trendRange * 86400;
            break;
        default:
            throw new IllegalArgumentException("Unsupported step: " + step);
    }
    result.getResults().forEach(resultValues -> {
        resultValues.getValues().forEach(mqeValue -> {
            if (!mqeValue.isEmptyValue()) {
                double newValue = mqeValue.getDoubleValue() / rangeSeconds;
                mqeValue.setDoubleValue(newValue);
            }
        });
    });
    return result;
}