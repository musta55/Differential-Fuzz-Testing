protected int getMaxThroughputAsInt() {
    JMeterProperty prop = getProperty(MAXTHROUGHPUT);
    int retVal = 1;
    if (prop instanceof IntegerProperty) {
        retVal = prop.getIntValue();
    } else {
        String valueString = prop.getStringValue();
        try {
            retVal = Integer.parseInt(valueString);
        } catch (NumberFormatException e) {
            log.warn("Error parsing '{}'", valueString, e);
        }
    }
    return retVal;
}