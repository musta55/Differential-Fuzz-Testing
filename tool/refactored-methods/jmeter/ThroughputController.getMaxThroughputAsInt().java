protected int getMaxThroughputAsInt() {
    return parseIntegerProperty(MAXTHROUGHPUT, 1);
}
// ---- helper method(s) introduced by the refactoring ----
private int parseIntegerProperty(String propertyName, int defaultValue) {
    JMeterProperty prop = getProperty(propertyName);
    if (prop instanceof IntegerProperty) {
        return prop.getIntValue();
    }
    String valueString = prop.getStringValue();
    try {
        return Integer.parseInt(valueString);
    } catch (NumberFormatException e) {
        log.warn("Error parsing '{}'", valueString, e);
        return defaultValue;
    }
}

private float parseFloatProperty(String propertyName, float defaultValue) {
    JMeterProperty prop = getProperty(propertyName);
    if (prop instanceof FloatProperty) {
        return prop.getFloatValue();
    }
    String valueString = prop.getStringValue();
    try {
        return Float.parseFloat(valueString);
    } catch (NumberFormatException e) {
        log.warn("Error parsing '{}'", valueString, e);
        return defaultValue;
    }
}

