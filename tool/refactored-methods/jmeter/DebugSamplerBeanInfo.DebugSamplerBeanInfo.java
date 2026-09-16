public DebugSamplerBeanInfo() {
    super(DebugSampler.class);
    setProperty("displayJMeterVariables", Boolean.TRUE);
    setProperty("displayJMeterProperties", Boolean.FALSE);
    setProperty("displaySystemProperties", Boolean.FALSE);
}
// ---- helper method(s) introduced by the refactoring ----
private void setProperty(String propertyName, Boolean defaultValue) {
    PropertyDescriptor p = property(propertyName);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(DEFAULT, defaultValue);
}

