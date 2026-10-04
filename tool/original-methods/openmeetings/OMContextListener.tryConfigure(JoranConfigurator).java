private void tryConfigure(JoranConfigurator configurator) throws JoranException, IOException {
    boolean configured = false;
    try (InputStream cfgIs = getClass().getResourceAsStream("/logback-test.xml")) {
        if (cfgIs != null) {
            configurator.doConfigure(cfgIs);
            configured = true;
        }
    } catch (Exception e) {
        // no-op
    }
    if (!configured) {
        try (InputStream cfgIs = getClass().getResourceAsStream("/logback-config.xml")) {
            configurator.doConfigure(cfgIs);
        }
    }
}