public RandomVariableConfigBeanInfo() {
    super(RandomVariableConfig.class);
    PropertyDescriptor p;
    createPropertyGroup(VARIABLE_GROUP, new String[] { VARIABLE_NAME, OUTPUT_FORMAT });
    p = property(VARIABLE_NAME);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p = property(OUTPUT_FORMAT);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    createPropertyGroup(RANDOM_GROUP, new String[] { MINIMUM_VALUE, MAXIMUM_VALUE, RANDOM_SEED });
    p = property(MINIMUM_VALUE);
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
    createPropertyGroup(OPTIONS_GROUP, new String[] { PER_THREAD });
    p = property(PER_THREAD);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
}