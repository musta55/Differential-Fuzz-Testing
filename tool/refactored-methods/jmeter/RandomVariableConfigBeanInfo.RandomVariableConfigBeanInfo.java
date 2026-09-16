public RandomVariableConfigBeanInfo() {
    super(RandomVariableConfig.class);
    setupVariableGroupProperties();
    setupRandomGroupProperties();
    setupOptionsGroupProperties();
}
// ---- helper method(s) introduced by the refactoring ----
private void setupVariableGroupProperties() {
    createPropertyGroup(VARIABLE_GROUP, new String[] { VARIABLE_NAME, OUTPUT_FORMAT });
    PropertyDescriptor p = property(VARIABLE_NAME);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p = property(OUTPUT_FORMAT);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
}

private void setupRandomGroupProperties() {
    createPropertyGroup(RANDOM_GROUP, new String[] { MINIMUM_VALUE, MAXIMUM_VALUE, RANDOM_SEED });
    PropertyDescriptor p = property(MINIMUM_VALUE);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "1");
    p = property(MAXIMUM_VALUE);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p = property(RANDOM_SEED);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
}

private void setupOptionsGroupProperties() {
    createPropertyGroup(OPTIONS_GROUP, new String[] { PER_THREAD });
    PropertyDescriptor p = property(PER_THREAD);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
}

