public static void main(String[] args) throws InterruptedException {
    setup();
    try {
        GlobalResultHolder globalResultHolder = BeanProvider.getContextualReference(GlobalResultHolder.class);
        while (globalResultHolder.getCount() < 100) {
            Thread.sleep(500);
            LOG.info("current count: " + globalResultHolder.getCount());
        }
        LOG.info("completed!");
    } finally {
        teardown();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void setup() {
    CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
    cdiContainer.boot();
    ContextControl contextControl = cdiContainer.getContextControl();
    contextControl.startContext(ApplicationScoped.class);
}

private static void teardown() {
    ContextControl contextControl = CdiContainerLoader.getCdiContainer().getContextControl();
    contextControl.stopContext(ApplicationScoped.class);
    CdiContainerLoader.getCdiContainer().shutdown();
}

