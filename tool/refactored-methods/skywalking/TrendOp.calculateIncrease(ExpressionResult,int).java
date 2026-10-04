private static ExpressionResult calculateIncrease(ExpressionResult expResult, int trendRange) {
    expResult.getResults().forEach(resultValues -> {
        List<MQEValue> mqeValues = resultValues.getValues();
        List<MQEValue> newMqeValues = new ArrayList<>();
        for (int i = trendRange; i < mqeValues.size(); i++) {
            MQEValue mqeValue = mqeValues.get(i);
            if (isValueEmpty(mqeValue)) {
                newMqeValues.add(mqeValue);
                continue;
            }
            MQEValue newMqeValue = new MQEValue();
            newMqeValue.setId(mqeValue.getId());
            MQEValue previousValue = mqeValues.get(i - trendRange);
            if (isValueEmpty(previousValue)) {
                newMqeValue.setEmptyValue(true);
                newMqeValues.add(newMqeValue);
                continue;
            }
            newMqeValue.setEmptyValue(mqeValue.isEmptyValue());
            newMqeValue.setId(mqeValue.getId());
            newMqeValue.setTraceID(mqeValue.getTraceID());
            double newValue = mqeValue.getDoubleValue() - previousValue.getDoubleValue();
            newMqeValue.setDoubleValue(newValue);
            newMqeValues.add(newMqeValue);
        }
        resultValues.setValues(newMqeValues);
    });
    return expResult;
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

