@Override
protected ConfigWatcherRegister initConfigReader() throws ModuleStartException {
    setSystemPropertiesFromSettings();
    return new ApolloConfigWatcherRegister(settings);
}
// ---- helper method(s) introduced by the refactoring ----
private void setSystemPropertiesFromSettings() {
    setSystemPropertyIfNotEmpty("apollo.cluster", settings.getApolloCluster());
    setSystemPropertyIfNotEmpty("apollo.meta", settings.getApolloMeta());
    setSystemPropertyIfNotEmpty("app.id", settings.getAppId());
    setSystemPropertyIfNotEmpty("env", settings.getApolloEnv());
}

private void setSystemPropertyIfNotEmpty(String propertyName, String propertyValue) {
    if (!Strings.isNullOrEmpty(propertyValue)) {
        System.setProperty(propertyName, propertyValue);
    }
}

