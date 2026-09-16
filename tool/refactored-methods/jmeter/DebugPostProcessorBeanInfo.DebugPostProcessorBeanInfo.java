public DebugPostProcessorBeanInfo() {
    super(DebugPostProcessor.class);
    setupDisplaySamplerProperties();
    setupDisplayJMeterVariables();
    setupDisplayJMeterProperties();
    setupDisplaySystemProperties();
}
// ---- helper method(s) introduced by the refactoring ----
private void setupDisplaySamplerProperties() {
    PropertyDescriptor p = property("displaySamplerProperties");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.TRUE);
}

private void setupDisplayJMeterVariables() {
    PropertyDescriptor p = property("displayJMeterVariables");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.TRUE);
}

private void setupDisplayJMeterProperties() {
    PropertyDescriptor p = property("displayJMeterProperties");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
}

private void setupDisplaySystemProperties() {
    PropertyDescriptor p = property("displaySystemProperties");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
}

