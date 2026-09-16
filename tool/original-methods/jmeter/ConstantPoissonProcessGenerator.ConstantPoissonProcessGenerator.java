public ConstantPoissonProcessGenerator(ThroughputProvider throughput, int batchSize, int batchThreadDelay, DurationProvider duration, Long seed, boolean logFirstSamples) {
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