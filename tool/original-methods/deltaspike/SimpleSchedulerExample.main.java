public static void main(String[] args) throws InterruptedException {
    CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
    cdiContainer.boot();
    ContextControl contextControl = cdiContainer.getContextControl();
    contextControl.startContext(ApplicationScoped.class);
    GlobalResultHolder globalResultHolder = BeanProvider.getContextualReference(GlobalResultHolder.class);
    while (globalResultHolder.getCount() < 100) {
        Thread.sleep(500);
        LOG.info("current count: " + globalResultHolder.getCount());
    }
    LOG.info("completed!");
    contextControl.stopContext(ApplicationScoped.class);
    cdiContainer.shutdown();
}