private EventProducer getEventProducer() {
    long testStarted = JMeterContextService.getTestStartTime();
    long prevStarted = PREV_TEST_STARTED.get();
    if (prevStarted != testStarted && PREV_TEST_STARTED.compareAndSet(prevStarted, testStarted)) {
        // Reset counters if we are calculating throughput for a new test, see https://github.com/apache/jmeter/issues/6165
        groupEvents.clear();
    }
    AbstractThreadGroup tg = getThreadContext().getThreadGroup();
    IdentityKey<AbstractThreadGroup> key = new IdentityKey<>(tg);
    EventProducer eventProducer = groupEvents.get(key);
    if (eventProducer != null) {
        return eventProducer;
    }
    Long seed = randomSeed == null || randomSeed == 0 ? null : randomSeed;
    return groupEvents.computeIfAbsent(key, x -> new ConstantPoissonProcessGenerator(() -> PreciseThroughputTimer.this.getThroughput() / throughputPeriod, batchSize, batchThreadDelay, this, seed, true));
}