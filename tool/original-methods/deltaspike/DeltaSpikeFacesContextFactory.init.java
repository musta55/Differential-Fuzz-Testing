private synchronized void init() {
    // switch into paranoia mode
    if (this.initialized == null) {
        this.clientWindow = BeanProvider.getContextualReference(ClientWindow.class, true);
        this.initialized = true;
    }
}