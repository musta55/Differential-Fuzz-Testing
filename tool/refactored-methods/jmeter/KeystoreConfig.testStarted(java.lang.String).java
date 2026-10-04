@Override
public void testStarted(String host) {
    String reuseSSLContext = JMeterUtils.getProperty("https.use.cached.ssl.context");
    if (StringUtils.isEmpty(reuseSSLContext) || "true".equals(reuseSSLContext)) {
        log.warn("https.use.cached.ssl.context property must be set to false to ensure Multiple Certificates are used");
    }
    int startIndexAsInt = parseIndex(startIndex, JMeterUtils.getPropDefault(KEY_STORE_START_INDEX, 0));
    int endIndexAsInt = parseIndex(endIndex, JMeterUtils.getPropDefault(KEY_STORE_END_INDEX, -1));
    if (endIndexAsInt != -1 && startIndexAsInt > endIndexAsInt) {
        throw new JMeterStopTestException("Keystore Config error : Alias start index must be lower than Alias end index");
    }
    log.info("Configuring Keystore with (preload: '{}', startIndex: {}, endIndex: {}, clientCertAliasVarName: '{}')", preload, startIndexAsInt, endIndexAsInt, clientCertAliasVarName);
    SSLManager.getInstance().configureKeystore(Boolean.parseBoolean(preload), startIndexAsInt, endIndexAsInt, clientCertAliasVarName);
}
// ---- helper method(s) introduced by the refactoring ----
private static int parseIndex(String indexStr, int defaultValue) {
    if (!StringUtils.isEmpty(indexStr)) {
        try {
            return Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            log.warn("Failed parsing index: {}, will default to: {}, error message: {}", indexStr, defaultValue, e.getMessage(), e);
        }
    }
    return defaultValue;
}

