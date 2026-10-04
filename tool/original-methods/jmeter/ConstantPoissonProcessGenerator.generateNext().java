public void generateNext() {
    double throughput = this.throughputProvider.getThroughput();
    lastThroughput = throughput;
    if (batchSize > 1) {
        throughput /= batchSize;
    }
    batchItemIndex = 0;
    long duration = this.durationProvider.getDuration();
    int samples = (int) Math.ceil(throughput * duration);
    ensureCapacity(samples);
    long t = System.currentTimeMillis();
    events.clear();
    for (int i = 0; i < samples; i++) {
        events.put(lastThroughputDurationFinish + rnd.nextDouble() * duration);
    }
    Arrays.sort(events.array(), events.arrayOffset(), events.position());
    t = System.currentTimeMillis() - t;
    if (t > 1000) {
        log.warn("Spent {} ms while generating sequence of delays for {} samples, {} throughput, {} duration", t, samples, throughput, duration);
    }
    lastThroughputDurationFinish += duration;
    if (logFirstSamples) {
        if (log.isDebugEnabled()) {
            log.debug("Generated {} events ({} required, rate {}) in {} ms", events.position(), samples, throughput, t);
        }
        if (log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Generated ").append(events.position()).append(" timings (");
            if (this.durationProvider instanceof AbstractTestElement) {
                sb.append(((AbstractTestElement) this.durationProvider).getName());
            }
            sb.append(" ").append(samples).append(" required, rate ").append(throughput).append(", duration ").append(duration).append(") in ").append(t).append(" ms");
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
    events.flip();
}