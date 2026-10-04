private void tryConfigure(JoranConfigurator configurator) throws JoranException, IOException {
    boolean configured = false;
    try (InputStream cfgIs = getClass().getResourceAsStream("/logback-test.xml")) {
        if (cfgIs != null) {
            configurator.doConfigure(cfgIs);
            configured = true;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    if (!configured) {
        try (InputStream cfgIs = getClass().getResourceAsStream("/logback-config.xml")) {
            configurator.doConfigure(cfgIs);
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setupLoggerConfiguration() {
    try {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.reset();
        tryConfigure(configurator);
    } catch (JoranException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

