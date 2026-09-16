public void release() {
    if (entityManagerDependentProvider != null) {
        entityManagerDependentProvider.destroy();
    }
    if (entityManagerResolverDependentProvider != null) {
        entityManagerResolverDependentProvider.destroy();
    }
}