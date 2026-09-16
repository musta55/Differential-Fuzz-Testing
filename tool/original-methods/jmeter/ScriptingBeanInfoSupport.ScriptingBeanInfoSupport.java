protected ScriptingBeanInfoSupport(Class<? extends TestBean> beanClass, String[] languageTags, ResourceBundle rb) {
    super(beanClass);
    PropertyDescriptor p;
    // $NON-NLS-1$
    p = property("scriptLanguage");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    if (JSR223TestElement.class.isAssignableFrom(beanClass)) {
        // $NON-NLS-1$
        p.setValue(DEFAULT, "groovy");
    } else {
        // $NON-NLS-1$
        p.setValue(DEFAULT, "");
    }
    if (rb != null) {
        p.setValue(RESOURCE_BUNDLE, rb);
    }
    p.setValue(TAGS, languageTags);
    // $NON-NLS-1$
    createPropertyGroup(// $NON-NLS-1$
    "scriptingLanguage", // $NON-NLS-1$
    new String[] { "scriptLanguage" });
    // $NON-NLS-1$
    p = property("parameters");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    // $NON-NLS-1$
    createPropertyGroup(// $NON-NLS-1$
    "parameterGroup", // $NON-NLS-1$
    new String[] { "parameters" });
    // $NON-NLS-1$
    p = property("filename");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setPropertyEditorClass(FileEditor.class);
    // $NON-NLS-1$
    createPropertyGroup(// $NON-NLS-1$
    "filenameGroup", // $NON-NLS-1$
    new String[] { "filename" });
    /*
         * If we are creating a JSR223 element, add the cache key property.
         *
         * Note that this cannot be done in the JSR223BeanInfoSupport class
         * because that causes problems with the group; its properties are
         * not always set up before they are needed. This cause various
         * issues with the GUI:
         * - wrong field attributes (should not allow null)
         * - sometimes GUI is completely mangled
         * - field appears at start rather than at end.
         * - the following warning is logged:
         * jmeter.testbeans.gui.GenericTestBeanCustomizer:
         * org.apache.jmeter.util.JSR223TestElement#cacheKey does not appear to have been configured
         *
         * Adding the group here solves these issues, and it's also
         * possible to add the key just before the script panel
         * to which it relates.
         *
         * It's not yet clear why this should be, but it looks as though
         * createPropertyGroup does not work properly if it is called from
         * any subclasses of this class.
         *
         */
    if (JSR223TestElement.class.isAssignableFrom(beanClass)) {
        // $NON-NLS-1$
        p = property("cacheKey");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(NOT_OTHER, Boolean.TRUE);
        // $NON-NLS-1$
        p.setValue(DEFAULT, TRUE_AS_STRING);
        p.setPropertyEditorClass(JSR223ScriptCacheCheckboxEditor.class);
        p.setValue(TAGS, new String[] { TRUE_AS_STRING, FALSE_AS_STRING });
        // $NON-NLS-1$
        createPropertyGroup(// $NON-NLS-1$
        "cacheKey_group", // $NON-NLS-1$
        new String[] { "cacheKey" });
    }
    // $NON-NLS-1$
    p = property("script");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setPropertyEditorClass(TextAreaEditor.class);
    // $NON-NLS-1$
    createPropertyGroup(// $NON-NLS-1$
    "scripting", // $NON-NLS-1$
    new String[] { "script" });
}