/**
 * Gets {@link SamplerCreator} for content type, if none is found returns {@link DefaultSamplerCreator}
 * @param request {@link HttpRequestHdr} from which the content type should be used
 * @param pageEncodings Map of pageEncodings
 * @param formEncodings  Map of formEncodings
 * @return SamplerCreator for the content type of the <code>request</code>, or {@link DefaultSamplerCreator} when none is found
 */
public SamplerCreator getSamplerCreator(HttpRequestHdr request, Map<String, String> pageEncodings, Map<String, String> formEncodings) {
    SamplerCreator creator = samplerCreatorMap.get(request.getContentType());
    return creator != null ? creator : DEFAULT_SAMPLER_CREATOR;
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

