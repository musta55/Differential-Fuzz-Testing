public void generateNext() {
    double throughput = calculateThroughput();
    int samples = calculateRequiredSamples(throughput);
    ensureCapacity(samples);
    generateEvents(throughput, samples);
    logGeneratedEvents(samples, throughput);
    events.flip();
}
// ---- helper method(s) introduced by the refactoring ----
private void initialize(ThroughputProvider throughput, int batchSize, int batchThreadDelay, DurationProvider duration, Long seed, boolean logFirstSamples) {
    this.throughputProvider = throughput;
    this.batchSize = batchSize;
    this.batchThreadDelay = batchThreadDelay;
    this.durationProvider = duration;
    this.logFirstSamples = logFirstSamples;
    if (seed != null && seed.intValue() != 0) {
        rnd.setSeed(seed);
    }
    ensureCapacity(0);
}

private double calculateThroughput() {
    double throughput = throughputProvider.getThroughput();
    lastThroughput = throughput;
    if (batchSize > 1) {
        throughput /= batchSize;
    }
    return throughput;
}

private int calculateRequiredSamples(double throughput) {
    long duration = durationProvider.getDuration();
    return (int) Math.ceil(throughput * duration);
}

private void generateEvents(double throughput, int samples) {
    long startTime = System.currentTimeMillis();
    events.clear();
    for (int i = 0; i < samples; i++) {
        events.put(lastThroughputDurationFinish + rnd.nextDouble() * durationProvider.getDuration());
    }
    Arrays.sort(events.array(), events.arrayOffset(), events.position());
    long generationTime = System.currentTimeMillis() - startTime;
    logGenerationTime(generationTime, samples, throughput);
    lastThroughputDurationFinish += durationProvider.getDuration();
}

private void logGenerationTime(long generationTime, int samples, double throughput) {
    if (generationTime > 1000) {
        log.warn("Spent {} ms while generating sequence of delays for {} samples, {} throughput, {} duration", generationTime, samples, throughput, durationProvider.getDuration());
    }
}

private void logGeneratedEvents(int samples, double throughput) {
    if (logFirstSamples) {
        if (log.isDebugEnabled()) {
            log.debug("Generated {} events ({} required, rate {}) in {} ms", events.position(), samples, throughput, System.currentTimeMillis() - lastThroughputDurationFinish);
        }
        if (log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Generated ").append(events.position()).append(" timings (");
            if (durationProvider instanceof AbstractTestElement) {
                sb.append(((AbstractTestElement) durationProvider).getName());
            }
            sb.append(" ").append(samples).append(" required, rate ").append(throughput).append(", duration ").append(durationProvider.getDuration()).append(") in ").append(System.currentTimeMillis() - lastThroughputDurationFinish).append(" ms");
            sb.append(". First 15 events will be fired at: ");
            double prev = 0;
            for (int i = 0; i < events.position() && i < 15; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                double ev = events.get(i);
                sb.append(ev);
                sb.append(" (+").append(ev - prev).append(")");
                prev = ev;
            }
            log.info(sb.toString());
        }
    }
}

private boolean shouldGenerateNext() {
    return (batchItemIndex == 0 && !events.hasRemaining()) || !valuesAreEqualWithPrecision(throughputProvider.getThroughput(), lastThroughput);
}

private double getNextEvent() {
    if (batchSize == 1) {
        return events.get();
    }
    batchItemIndex++;
    if (batchItemIndex == 1) {
        return events.get();
    }
    if (batchItemIndex == batchSize) {
        batchItemIndex = 0;
    }
    return events.get(events.position() - 1);
}

