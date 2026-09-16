public MongoScriptSamplerBeanInfo() {
    super(MongoScriptSampler.class);
    //http://api.mongodb.org/java/2.7.2/com/mongodb/Mongo.html
    createPropertyGroup("mongodb", new String[] { "source", "database", "username", "password" });
    createPropertyGroup("sampler", new String[] { "script" });
    PropertyDescriptor p = property("database");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "");
    p = property("username");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "");
    p = property("password", TypeEditor.PasswordEditor);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "");
    p = property("source");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "");
    p = property("script", TypeEditor.TextAreaEditor);
    p.setValue(NOT_UNDEFINED, Boolean.FALSE);
    p.setValue(DEFAULT, "");
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(TEXT_LANGUAGE, "javascript");
    if (log.isDebugEnabled()) {
        for (PropertyDescriptor pd : getPropertyDescriptors()) {
            log.debug(pd.getName());
            log.debug(pd.getDisplayName());
        }
    }
}