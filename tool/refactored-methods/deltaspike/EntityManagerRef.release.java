public void release() {
    destroyDependentProvider(entityManagerDependentProvider);
    destroyDependentProvider(entityManagerResolverDependentProvider);
}
// ---- helper method(s) introduced by the refactoring ----
private void destroyDependentProvider(DependentProvider<?> provider) {
    if (provider != null) {
        provider.destroy();
    }
}

