private static ExpressionResult calculateIncrease(ExpressionResult expResult, int trendRange) {
    expResult.getResults().forEach(resultValues -> {
        List<MQEValue> mqeValues = resultValues.getValues();
        List<MQEValue> newMqeValues = new ArrayList<>();
        for (int i = trendRange; i < mqeValues.size(); i++) {
            MQEValue mqeValue = mqeValues.get(i);
            //if the current value is empty, then the trend value is empty
            if (mqeValue.isEmptyValue()) {
                newMqeValues.add(mqeValue);
                continue;
            }
            MQEValue newMqeValue = new MQEValue();
            newMqeValue.setId(mqeValue.getId());
            //if the previous value is empty, then the trend value is empty
            if (mqeValues.get(i - trendRange).isEmptyValue()) {
                newMqeValue.setEmptyValue(true);
                newMqeValues.add(newMqeValue);
                continue;
            }
            newMqeValue.setEmptyValue(mqeValue.isEmptyValue());
            newMqeValue.setId(mqeValue.getId());
            newMqeValue.setTraceID(mqeValue.getTraceID());
            double newValue = mqeValue.getDoubleValue() - mqeValues.get(i - trendRange).getDoubleValue();
            newMqeValue.setDoubleValue(newValue);
            newMqeValues.add(newMqeValue);
        }
        resultValues.setValues(newMqeValues);
    });
    return expResult;
}