public DataSourceElementBeanInfo() {
    super(DataSourceElement.class);
    createPropertyGroups();
    setPropertyDefaults();
}
// ---- helper method(s) introduced by the refactoring ----
private void createPropertyGroups() {
    createPropertyGroup("varName", new String[] { "dataSource" });
    createPropertyGroup("pool", new String[] { "poolMax", "timeout", "trimInterval", "autocommit", "transactionIsolation", "poolPreparedStatements", "preinit", "initQuery" });
    createPropertyGroup("keep-alive", new String[] { "keepAlive", "connectionAge", "checkQuery" });
    createPropertyGroup("database", new String[] { "dbUrl", "driver", "username", "password", "connectionProperties" });
}

private void setPropertyDefaults() {
    setDataSourceProperty();
    setPoolProperties();
    setKeepAliveProperties();
    setDatabaseProperties();
}

private void setDataSourceProperty() {
    PropertyDescriptor p = property("dataSource");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "");
}

private void setPoolProperties() {
    setProperty("poolMax", "0");
    setProperty("timeout", "10000");
    setProperty("trimInterval", "60000");
    setProperty("autocommit", Boolean.TRUE.toString());
    setTransactionIsolationProperty();
    setProperty("poolPreparedStatements", "-1", DEFAULT_NOT_SAVED);
    setProperty("preinit", Boolean.FALSE.toString());
    setProperty("initQuery", "", TypeEditor.TextAreaEditor);
}

private void setTransactionIsolationProperty() {
    PropertyDescriptor p = property("transactionIsolation");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "DEFAULT");
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    Set<String> modesSet = TRANSACTION_ISOLATION_MAP.keySet();
    String[] modes = modesSet.toArray(new String[0]);
    p.setValue(TAGS, modes);
}

private void setKeepAliveProperties() {
    setProperty("keepAlive", Boolean.TRUE.toString());
    setProperty("connectionAge", "5000");
    setProperty("checkQuery", "", TypeEditor.ComboStringEditor, getListCheckQuery());
}

private void setDatabaseProperties() {
    setProperty("dbUrl", "");
    setProperty("driver", "", TypeEditor.ComboStringEditor, getListJDBCDriverClass());
    setProperty("username", "");
    setProperty("password", "", TypeEditor.PasswordEditor);
    setProperty("connectionProperties", "");
}

private void setProperty(String propertyName, String defaultValue) {
    setProperty(propertyName, defaultValue, null);
}

private void setProperty(String propertyName, String defaultValue, Object defaultNotSaved) {
    PropertyDescriptor p = property(propertyName);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    if (defaultNotSaved != null) {
        p.setValue(DEFAULT_NOT_SAVED, defaultNotSaved);
    }
    p.setValue(DEFAULT, defaultValue);
}

private void setProperty(String propertyName, String defaultValue, Object editor, String[] tags) {
    PropertyDescriptor p = property(propertyName);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, defaultValue);
    p.setPropertyEditorClass((Class<? extends java.beans.PropertyEditor>) editor);
    p.setValue(TAGS, tags);
}

