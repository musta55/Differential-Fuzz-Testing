public static void main(String[] args) {
    CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
    cdiContainer.boot();
    ContextControl contextControl = cdiContainer.getContextControl();
    contextControl.startContext(ApplicationScoped.class);
    SettingsBean settingsBean = BeanProvider.getContextualReference(SettingsBean.class, false);
    LOG.info("configured int-value #1: " + settingsBean.getIntProperty1());
    LOG.info("configured long-value #2: " + settingsBean.getProperty2());
    LOG.info("configured inverse-value #2: " + settingsBean.getInverseProperty());
    LOG.info("configured location (custom config): " + settingsBean.getLocationId().name());
    cdiContainer.shutdown();
}