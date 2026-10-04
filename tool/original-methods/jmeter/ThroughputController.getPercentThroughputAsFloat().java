protected float getPercentThroughputAsFloat() {
    JMeterProperty prop = getProperty(PERCENTTHROUGHPUT);
    float retVal = 100;
    if (prop instanceof FloatProperty) {
        retVal = prop.getFloatValue();
    } else {
        String valueString = prop.getStringValue();
        try {
            retVal = Float.parseFloat(valueString);
        } catch (NumberFormatException e) {
            log.warn("Error parsing '{}'", valueString, e);
        }
    }
    return retVal;
}