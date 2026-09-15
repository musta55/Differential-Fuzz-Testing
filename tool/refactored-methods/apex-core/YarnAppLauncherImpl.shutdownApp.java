protected void shutdownApp(YarnAppHandleImpl app, ShutdownMode shutdownMode) throws LauncherException {
    if (shutdownMode == ShutdownMode.KILL) {
        try {
            ApplicationId applicationId = app.appId;
            ApplicationReport appReport = app.yarnClient.getApplicationReport(applicationId);
            if (appReport == null) {
                throw new LauncherException("Application " + app.getApplicationId() + " not found");
            }
            app.yarnClient.killApplication(applicationId);
        } catch (YarnException | IOException e) {
            throw new LauncherException(e);
        }
    } else {
        throw new UnsupportedOperationException("Orderly shutdown not supported, try kill instead");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void extractConfigurationSettings(Attribute.AttributeMap launchParameters, Configuration conf) {
    if (launchParameters != null) {
        for (Map.Entry<Attribute<?>, Object> entry : launchParameters.entrySet()) {
            String property = propMapping.get(entry.getKey());
            if (property != null) {
                setConfiguration(conf, property, entry.getValue());
            }
        }
    }
}

