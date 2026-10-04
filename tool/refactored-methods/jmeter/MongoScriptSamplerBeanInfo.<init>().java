public MongoScriptSamplerBeanInfo() {
    super(MongoScriptSampler.class);
    setupMongoDBProperties();
    setupSamplerProperties();
    logProperties();
}
// ---- helper method(s) introduced by the refactoring ----
private void setupMongoDBProperties() {
    createPropertyGroup("mongodb", new String[] { "source", "database", "username", "password" });
    setProperty("database", true, "");
    setProperty("username", true, "");
    setProperty("password", TypeEditor.PasswordEditor, true, "");
    setProperty("source", true, "");
}

private void setupSamplerProperties() {
    createPropertyGroup("sampler", new String[] { "script" });
    setProperty("script", TypeEditor.TextAreaEditor, false, "", true, "javascript");
}

private void setProperty(String propertyName, boolean notUndefined, String defaultValue) {
    PropertyDescriptor p = property(propertyName);
    p.setValue(NOT_UNDEFINED, notUndefined);
    p.setValue(DEFAULT, defaultValue);
}

private void setProperty(String propertyName, TypeEditor editor, boolean notUndefined, String defaultValue) {
    PropertyDescriptor p = property(propertyName, editor);
    p.setValue(NOT_UNDEFINED, notUndefined);
    p.setValue(DEFAULT, defaultValue);
}

private void setProperty(String propertyName, TypeEditor editor, boolean notUndefined, String defaultValue, boolean notExpression, String textLanguage) {
    PropertyDescriptor p = property(propertyName, editor);
    p.setValue(NOT_UNDEFINED, notUndefined);
    p.setValue(DEFAULT, defaultValue);
    p.setValue(NOT_EXPRESSION, notExpression);
    p.setValue(TEXT_LANGUAGE, textLanguage);
}

private void logProperties() {
    if (log.isDebugEnabled()) {
        for (PropertyDescriptor pd : getPropertyDescriptors()) {
            log.debug(pd.getName());
            log.debug(pd.getDisplayName());
        }
    }
}

