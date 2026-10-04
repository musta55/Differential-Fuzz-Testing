/**
 * Initialize factory from classpath
 */
private void init() {
    // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
    for (SamplerCreator creator : JMeterUtils.loadServicesAndScanJars(SamplerCreator.class, ServiceLoader.load(SamplerCreator.class), Thread.currentThread().getContextClassLoader(), new LogAndIgnoreServiceLoadExceptionHandler(log))) {
        try {
            String[] contentTypes = creator.getManagedContentTypes();
            for (String contentType : contentTypes) {
                log.debug("Registering samplerCreator {} for content type:{}", creator.getClass().getName(), contentType);
                SamplerCreator oldSamplerCreator = samplerCreatorMap.put(contentType, creator);
                if (oldSamplerCreator != null) {
                    log.warn("A sampler creator was already registered for:{}, class:{}, it will be replaced", contentType, oldSamplerCreator.getClass());
                }
            }
        } catch (Exception e) {
            log.error("Exception registering {} with implementation:{}", SamplerCreator.class.getName(), creator.getClass(), e);
        }
    }
}