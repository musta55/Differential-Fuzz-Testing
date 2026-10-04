public BoltConnectionElementBeanInfo() {
    super(BoltConnectionElement.class);
    createPropertyGroup("connection", new String[] { "boltUri", "username", "password", "maxConnectionPoolSize" });
    createBoltUriPropertyDescriptor();
    createUsernamePropertyDescriptor();
    createPasswordPropertyDescriptor();
    createMaxConnectionPoolSizePropertyDescriptor();
    if (log.isDebugEnabled()) {
        String descriptorsAsString = Arrays.stream(getPropertyDescriptors()).map(pd -> pd.getName() + "=" + pd.getDisplayName()).collect(Collectors.joining(", "));
        log.debug(descriptorsAsString);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void createBoltUriPropertyDescriptor() {
    PropertyDescriptor propertyDescriptor = property("boltUri");
    propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
    propertyDescriptor.setValue(DEFAULT, "bolt://localhost:7687");
}

private void createUsernamePropertyDescriptor() {
    PropertyDescriptor propertyDescriptor = property("username");
    propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
    propertyDescriptor.setValue(DEFAULT, "neo4j");
}

private void createPasswordPropertyDescriptor() {
    PropertyDescriptor propertyDescriptor = property("password", TypeEditor.PasswordEditor);
    propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
    propertyDescriptor.setValue(DEFAULT, "");
}

private void createMaxConnectionPoolSizePropertyDescriptor() {
    PropertyDescriptor propertyDescriptor = property("maxConnectionPoolSize");
    propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
    propertyDescriptor.setValue(DEFAULT, 100);
}

