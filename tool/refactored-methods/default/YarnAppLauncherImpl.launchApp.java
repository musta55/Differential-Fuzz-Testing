@Override
public YarnAppHandleImpl launchApp(final StreamingApplication app, Configuration conf, Attribute.AttributeMap launchParameters) throws LauncherException {
    extractConfigurationSettings(launchParameters, conf);
    try {
        String name = app.getClass().getName();
        StramAppLauncher appLauncher = new StramAppLauncher(name, conf);
        appLauncher.loadDependencies();
        StreamingAppFactory appFactory = new StreamingAppFactory(name, app.getClass()) {

            @Override
            public LogicalPlan createApp(LogicalPlanConfiguration planConfig) {
                return super.createApp(app, planConfig);
            }
        };
        ApplicationId appId = appLauncher.launchApp(appFactory);
        appLauncher.resetContextClassLoader();
        return new YarnAppHandleImpl(appId, conf);
    } catch (Exception ex) {
        throw new LauncherException(ex);
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

