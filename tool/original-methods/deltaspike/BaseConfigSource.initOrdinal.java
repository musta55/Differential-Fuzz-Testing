/**
 * Init method e.g. for initializing the ordinal.
 * This method can be used from a subclass to determine
 * the ordinal value
 * @param defaultOrdinal the default value for the ordinal if not set via configuration
 */
protected void initOrdinal(int defaultOrdinal) {
    ordinal = defaultOrdinal;
    String configuredOrdinalString = getPropertyValue(ConfigSource.DELTASPIKE_ORDINAL);
    try {
        if (configuredOrdinalString != null) {
            ordinal = Integer.parseInt(configuredOrdinalString.trim());
        }
    } catch (NumberFormatException e) {
        log.log(Level.WARNING, "The configured config-ordinal isn't a valid integer. Invalid value: " + configuredOrdinalString);
    }
}