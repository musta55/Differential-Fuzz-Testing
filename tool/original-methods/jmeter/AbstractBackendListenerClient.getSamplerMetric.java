/**
 * @param sampleLabel Name of sample used as key
 * @return {@link SamplerMetric}
 */
protected final SamplerMetric getSamplerMetric(String sampleLabel) {
    SamplerMetric samplerMetric = metricsPerSampler.get(sampleLabel);
    if (samplerMetric == null) {
        samplerMetric = new SamplerMetric();
        SamplerMetric oldValue = metricsPerSampler.putIfAbsent(sampleLabel, samplerMetric);
        if (oldValue != null) {
            samplerMetric = oldValue;
        }
    }
    return samplerMetric;
}