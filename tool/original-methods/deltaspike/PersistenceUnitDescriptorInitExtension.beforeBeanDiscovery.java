void beforeBeanDiscovery(@Observes BeforeBeanDiscovery before) {
    isActivated = ClassDeactivationUtils.isActivated(getClass());
    if (!isActivated) {
        return;
    }
    PersistenceUnitDescriptorProvider.getInstance().init();
}