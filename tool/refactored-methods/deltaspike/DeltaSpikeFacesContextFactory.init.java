private synchronized void init() {
    if (initialized == null) {
        clientWindow = BeanProvider.getContextualReference(ClientWindow.class, true);
        initialized = true;
    }
}