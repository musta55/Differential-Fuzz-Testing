private static ExpressionResult calculateRate(ExpressionResult expResult, int trendRange, Step step) {
    ExpressionResult result = calculateIncrease(expResult, trendRange);
    long rangeSeconds = calculateRangeSeconds(trendRange, step);
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
// ---- helper method(s) introduced by the refactoring ----
private static boolean isValueEmpty(MQEValue mqeValue) {
    return mqeValue.isEmptyValue();
}

private static long calculateRangeSeconds(int trendRange, Step step) {
    switch(step) {
        case SECOND:
            return trendRange;
        case MINUTE:
            return trendRange * 60;
        case HOUR:
            return trendRange * 3600;
        case DAY:
            return trendRange * 86400;
        default:
            throw new IllegalArgumentException("Unsupported step: " + step);
    }
}

