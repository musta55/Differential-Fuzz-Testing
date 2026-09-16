@Override
public void contextInitialized(ServletContextEvent event) {
    String ctx = pathToName(event);
    System.setProperty(CTX_NAME_PROP, ctx);
    if (Strings.isEmpty(System.getProperty(LOG_DIR_PROP))) {
        System.setProperty(LOG_DIR_PROP, "logs");
    }
    System.setProperty("webapp.contextPath", String.format("/%s", ctx));
    setupLoggerConfiguration();
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

