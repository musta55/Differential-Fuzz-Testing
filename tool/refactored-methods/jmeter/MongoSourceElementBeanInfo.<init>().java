public MongoSourceElementBeanInfo() {
    super(MongoSourceElement.class);
    setupMongoDBProperties();
    setupOptionsProperties();
    setupWriteConcernProperties();
    setupDefaultValues();
    if (log.isDebugEnabled()) {
        for (PropertyDescriptor pd : getPropertyDescriptors()) {
            log.debug(pd.getName());
            log.debug(pd.getDisplayName());
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setupMongoDBProperties() {
    createPropertyGroup("mongodb", new String[] { "connection", "source" });
}

private void setupOptionsProperties() {
    createPropertyGroup("options", new String[] { "autoConnectRetry", "connectionsPerHost", "connectTimeout", "maxAutoConnectRetryTime", "maxWaitTime", "socketTimeout", "socketKeepAlive", "threadsAllowedToBlockForConnectionMultiplier" });
}

private void setupWriteConcernProperties() {
    createPropertyGroup("writeConcern", new String[] { "safe", "fsync", "waitForJournaling", "writeOperationNumberOfServers", "writeOperationTimeout", "continueOnInsertError" });
}

private void setupDefaultValues() {
    setDefaultValue("connection", "", true);
    setDefaultValue("source", "", true);
    setDefaultValue("autoConnectRetry", Boolean.FALSE, true);
    setDefaultValue("connectionsPerHost", 10, true);
    setDefaultValue("connectTimeout", 0, true);
    setDefaultValue("threadsAllowedToBlockForConnectionMultiplier", 5, true);
    setDefaultValue("maxAutoConnectRetryTime", 0L, true);
    setDefaultValue("maxWaitTime", 120000, true);
    setDefaultValue("socketTimeout", 0, true);
    setDefaultValue("socketKeepAlive", Boolean.FALSE, true);
    setDefaultValue("fsync", Boolean.FALSE, true);
    setDefaultValue("safe", Boolean.FALSE, true);
    setDefaultValue("waitForJournaling", Boolean.FALSE, true);
    setDefaultValue("writeOperationNumberOfServers", 0, true);
    setDefaultValue("writeOperationTimeout", 0, true);
    setDefaultValue("continueOnInsertError", Boolean.FALSE, true);
}

private void setDefaultValue(String propertyName, Object defaultValue, boolean notUndefined) {
    PropertyDescriptor p = property(propertyName);
    p.setValue(NOT_UNDEFINED, notUndefined);
    p.setValue(DEFAULT, defaultValue);
}

