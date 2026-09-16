@Override
public void testEnded() {
    synchronized (this) {
        closeAndRemoveDriver();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void createAndStoreDriver(JMeterVariables variables) {
    Config config = Config.builder().withMaxConnectionPoolSize(getMaxConnectionPoolSize()).build();
    driver = GraphDatabase.driver(getBoltUri(), AuthTokens.basic(getUsername(), getPassword()), config);
    variables.putObject(BOLT_CONNECTION, driver);
}

private void closeAndRemoveDriver() {
    if (driver != null) {
        driver.close();
        driver = null;
    }
}

