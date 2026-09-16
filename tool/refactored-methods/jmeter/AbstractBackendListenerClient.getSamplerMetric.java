/**
 * @param sampleLabel Name of sample used as key
 * @return {@link SamplerMetric}
 */
protected final SamplerMetric getSamplerMetric(String sampleLabel) {
    return metricsPerSampler.computeIfAbsent(sampleLabel, k -> new SamplerMetric());
}
// ---- helper method(s) introduced by the refactoring ----
private void clearMetrics() {
    metricsPerSampler.clear();
    userMetrics.clear();
}

