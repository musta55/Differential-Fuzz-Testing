public CompareAssertionBeanInfo() {
    super(CompareAssertion.class);
    createPropertyGroups();
    setCompareContentProperty();
    setCompareTimeProperty();
    setStringsToSkipProperty();
}
// ---- helper method(s) introduced by the refactoring ----
private void createPropertyGroups() {
    //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
    createPropertyGroup("compareChoices", new String[] { "compareContent", "compareTime" });
    //$NON-NLS-1$ $NON-NLS-2$
    createPropertyGroup("comparison_filters", new String[] { "stringsToSkip" });
}

private void setCompareContentProperty() {
    //$NON-NLS-1$
    PropertyDescriptor p = property("compareContent");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
}

private void setCompareTimeProperty() {
    //$NON-NLS-1$
    PropertyDescriptor p = property("compareTime");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, -1L);
    p.setValue(NOT_EXPRESSION, Boolean.FALSE);
}

private void setStringsToSkipProperty() {
    //$NON-NLS-1$
    PropertyDescriptor p = property("stringsToSkip");
    p.setPropertyEditorClass(TableEditor.class);
    p.setValue(TableEditor.CLASSNAME, SubstitutionElement.class.getName());
    p.setValue(TableEditor.HEADERS, new String[] { //$NON-NLS-1$
    JMeterUtils.getResString("comparison_regex_string"), //$NON-NLS-1$
    JMeterUtils.getResString("comparison_regex_substitution") });
    // These are the names of the get/set methods
    p.// These are the names of the get/set methods
    setValue(// These are the names of the get/set methods
    TableEditor.OBJECT_PROPERTIES, new String[] { SubstitutionElement.REGEX, SubstitutionElement.SUBSTITUTE });
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, new ArrayList<>());
    p.setValue(MULTILINE, Boolean.TRUE);
}

