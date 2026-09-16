/**
 * Init method e.g. for initializing the ordinal.
 * This method can be used from a subclass to determine
 * the ordinal value
 * @param defaultOrdinal the default value for the ordinal if not set via configuration
 */
protected void initOrdinal(int defaultOrdinal) {
    ordinal = defaultOrdinal;
    String configuredOrdinalString = fetchConfiguredOrdinal();
    if (configuredOrdinalString != null) {
        parseAndSetOrdinal(configuredOrdinalString);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private String fetchConfiguredOrdinal() {
    return getPropertyValue(ConfigSource.DELTASPIKE_ORDINAL);
}

private void parseAndSetOrdinal(String configuredOrdinalString) {
    try {
        ordinal = Integer.parseInt(configuredOrdinalString.trim());
    } catch (NumberFormatException e) {
        logInvalidOrdinal(configuredOrdinalString);
    }
}

private void logInvalidOrdinal(String configuredOrdinalString) {
    log.log(Level.WARNING, "The configured config-ordinal isn't a valid integer. Invalid value: " + configuredOrdinalString);
}

