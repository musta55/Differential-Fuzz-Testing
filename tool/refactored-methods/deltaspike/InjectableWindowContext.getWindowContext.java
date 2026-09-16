private WindowContext getWindowContext() {
    if (windowContext == null) {
        windowContext = BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getWindowContext();
    }
    return windowContext;
}