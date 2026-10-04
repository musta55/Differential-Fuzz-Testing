/**
 * Set the counter for all available {@link SamplerCreator}s.
 * <p>
 * <em>The only implementation that is currently available, increments the counter before it is used!</em>
 * @param value to initialize the creators
 */
public void setCounter(int value) {
    DEFAULT_SAMPLER_CREATOR.setCounter(value);
    updateCountersForAllCreators(value);
}
// ---- helper method(s) introduced by the refactoring ----
private void updateCountersForAllCreators(int value) {
    for (SamplerCreator samplerCreator : samplerCreatorMap.values()) {
        samplerCreator.setCounter(value);
    }
}

private void loadAndRegisterSamplers() {
    for (SamplerCreator creator : JMeterUtils.loadServicesAndScanJars(SamplerCreator.class, ServiceLoader.load(SamplerCreator.class), Thread.currentThread().getContextClassLoader(), new LogAndIgnoreServiceLoadExceptionHandler(log))) {
        registerSamplerCreator(creator);
    }
}

private void registerSamplerCreator(SamplerCreator creator) {
    try {
        for (String contentType : creator.getManagedContentTypes()) {
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

