public static void main(String[] args) {
    CdiContainer cdiContainer = null;
    try {
        cdiContainer = setupCdiContainer();
        SettingsBean settingsBean = BeanProvider.getContextualReference(SettingsBean.class, false);
        LOG.info("configured int-value #1: " + settingsBean.getIntProperty1());
        LOG.info("configured long-value #2: " + settingsBean.getProperty2());
        LOG.info("configured inverse-value #2: " + settingsBean.getInverseProperty());
        LOG.info("configured location (custom config): " + settingsBean.getLocationId().name());
    } finally {
        if (cdiContainer != null) {
            shutdownCdiContainer(cdiContainer);
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static CdiContainer setupCdiContainer() {
    CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
    cdiContainer.boot();
    ContextControl contextControl = cdiContainer.getContextControl();
    contextControl.startContext(ApplicationScoped.class);
    return cdiContainer;
}

private static void shutdownCdiContainer(CdiContainer cdiContainer) {
    cdiContainer.shutdown();
}

