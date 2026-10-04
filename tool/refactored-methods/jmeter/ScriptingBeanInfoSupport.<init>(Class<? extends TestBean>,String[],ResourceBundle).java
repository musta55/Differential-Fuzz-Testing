protected ScriptingBeanInfoSupport(Class<? extends TestBean> beanClass, String[] languageTags, ResourceBundle rb) {
    super(beanClass);
    setupScriptLanguageProperty(beanClass, languageTags, rb);
    setupParametersProperty();
    setupFilenameProperty();
    if (JSR223TestElement.class.isAssignableFrom(beanClass)) {
        setupCacheKeyProperty();
    }
    setupScriptProperty();
}
// ---- helper method(s) introduced by the refactoring ----
private void setupScriptLanguageProperty(Class<? extends TestBean> beanClass, String[] languageTags, ResourceBundle rb) {
    // $NON-NLS-1$
    PropertyDescriptor p = property("scriptLanguage");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, JSR223TestElement.class.isAssignableFrom(beanClass) ? "groovy" : "");
    if (rb != null) {
        p.setValue(RESOURCE_BUNDLE, rb);
    }
    p.setValue(TAGS, languageTags);
    // $NON-NLS-1$
    createPropertyGroup("scriptingLanguage", new String[] { "scriptLanguage" });
}

private void setupParametersProperty() {
    // $NON-NLS-1$
    PropertyDescriptor p = property("parameters");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    // $NON-NLS-1$
    createPropertyGroup("parameterGroup", new String[] { "parameters" });
}

private void setupFilenameProperty() {
    // $NON-NLS-1$
    PropertyDescriptor p = property("filename");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setPropertyEditorClass(FileEditor.class);
    // $NON-NLS-1$
    createPropertyGroup("filenameGroup", new String[] { "filename" });
}

private void setupCacheKeyProperty() {
    // $NON-NLS-1$
    PropertyDescriptor p = property("cacheKey");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, TRUE_AS_STRING);
    p.setPropertyEditorClass(JSR223ScriptCacheCheckboxEditor.class);
    p.setValue(TAGS, new String[] { TRUE_AS_STRING, FALSE_AS_STRING });
    // $NON-NLS-1$
    createPropertyGroup("cacheKey_group", new String[] { "cacheKey" });
}

private void setupScriptProperty() {
    // $NON-NLS-1$
    PropertyDescriptor p = property("script");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setPropertyEditorClass(TextAreaEditor.class);
    // $NON-NLS-1$
    createPropertyGroup("scripting", new String[] { "script" });
}

