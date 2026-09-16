@Override
public void close() {
    getViewAccessContextManager().close();
}
// ---- helper method(s) introduced by the refactoring ----
private ViewAccessContextManager getViewAccessContextManager() {
    if (this.viewAccessContextManager == null) {
        this.viewAccessContextManager = BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getViewAccessScopedContext();
    }
    return this.viewAccessContextManager;
}

