/**
 * Reading system property as long value.
 * @param propertyName Name of the system property
 * @param defaultValue Default value to return in case of an error, out of range etc.
 * @param minValue minimum valid value
 * @param maxValue maximum valid value
 * @return returns the value if it is between min and max value(inclusive), otherwise default value is returned.
 */
public static long getLong(String propertyName, long defaultValue, long minValue, long maxValue) {
    String property = System.getProperty(propertyName);
    long result = defaultValue;
    if (property != null) {
        try {
            long value = Long.decode(property);
            if (value < minValue || value > maxValue) {
                logger.warn("Property {} is outside the range [{},{}], setting to default {}", propertyName, minValue, maxValue, defaultValue);
            } else {
                result = value;
            }
        } catch (Exception ex) {
            logger.warn("Can't convert property {} value {} to a long, using default {}", propertyName, property, defaultValue, ex);
        }
    }
    logger.debug("System property {}'s value is {}", propertyName, result);
    return result;
}