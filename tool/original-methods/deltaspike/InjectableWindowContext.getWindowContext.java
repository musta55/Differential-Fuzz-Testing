private WindowContext getWindowContext() {
    if (this.windowContext == null) {
        this.windowContext = BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getWindowContext();
    }
    return this.windowContext;
}