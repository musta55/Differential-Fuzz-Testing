@Override
public void testStarted() {
    this.setRunningVersion(true);
    TestBeanHelper.prepare(this);
    JMeterVariables variables = getThreadContext().getVariables();
    if (variables.getObject(BOLT_CONNECTION) != null) {
        log.error("Bolt connection already exists");
    } else {
        synchronized (this) {
            Config config = Config.builder().withMaxConnectionPoolSize(getMaxConnectionPoolSize()).build();
            driver = GraphDatabase.driver(getBoltUri(), AuthTokens.basic(getUsername(), getPassword()), config);
            variables.putObject(BOLT_CONNECTION, driver);
        }
    }
}