void beforeBeanDiscovery(@Observes BeforeBeanDiscovery before) {
    isActivated = checkActivation();
    if (!isActivated) {
        return;
    }
    PersistenceUnitDescriptorProvider.getInstance().init();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean checkActivation() {
    return ClassDeactivationUtils.isActivated(getClass());
}

