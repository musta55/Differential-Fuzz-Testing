@Override
public void testStarted(String host) {
    String reuseSSLContext = JMeterUtils.getProperty("https.use.cached.ssl.context");
    if (StringUtils.isEmpty(reuseSSLContext) || "true".equals(reuseSSLContext)) {
        log.warn("https.use.cached.ssl.context property must be set to false to ensure Multiple Certificates are used");
    }
    int startIndexAsInt = JMeterUtils.getPropDefault(KEY_STORE_START_INDEX, 0);
    int endIndexAsInt = JMeterUtils.getPropDefault(KEY_STORE_END_INDEX, -1);
    if (!StringUtils.isEmpty(this.startIndex)) {
        try {
            startIndexAsInt = Integer.parseInt(this.startIndex);
        } catch (NumberFormatException e) {
            log.warn("Failed parsing startIndex: {}, will default to: {}, error message: {}", this.startIndex, startIndexAsInt, e, e);
        }
    }
    if (!StringUtils.isEmpty(this.endIndex)) {
        try {
            endIndexAsInt = Integer.parseInt(this.endIndex);
        } catch (NumberFormatException e) {
            log.warn("Failed parsing endIndex: {}, will default to: {}, error message: {}", this.endIndex, endIndexAsInt, e, e);
        }
    }
    if (endIndexAsInt != -1 && startIndexAsInt > endIndexAsInt) {
        throw new JMeterStopTestException("Keystore Config error : Alias start index must be lower than Alias end index");
    }
    log.info("Configuring Keystore with (preload: '{}', startIndex: {}, endIndex: {}, clientCertAliasVarName: '{}')", preload, startIndexAsInt, endIndexAsInt, clientCertAliasVarName);
    SSLManager.getInstance().configureKeystore(Boolean.parseBoolean(preload), startIndexAsInt, endIndexAsInt, clientCertAliasVarName);
}