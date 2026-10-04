@Override
public long delay() {
    double nextEvent = getNextEvent();
    long now = System.currentTimeMillis();
    long testStarted = JMeterContextService.getTestStartTime();
    long delay = calculateDelay(nextEvent, now, testStarted);
    if (log.isDebugEnabled()) {
        log.debug("Calculated delay is {}", delay);
    }
    delay = Math.max(0, delay);
    long endTime = getThreadContext().getThread().getEndTime();
    if (endTime > 0 && now + delay > endTime) {
        throw new JMeterStopThreadException("The thread is scheduled to stop in " + (endTime - now) + " ms" + " and the throughput timer generates a delay of " + delay + "." + " Terminating the thread manually.");
    }
    return delay;
}
// ---- helper method(s) introduced by the refactoring ----
private double getNextEvent() {
    EventProducer events = getEventProducer();
    synchronized (events) {
        return events.next();
    }
}

private static long calculateDelay(double nextEvent, long now, long testStarted) {
    return (long) (nextEvent * TimeUnit.SECONDS.toMillis(1) + testStarted - now);
}

private EventProducer createEventProducer(Long seed) {
    return new ConstantPoissonProcessGenerator(() -> getThroughput() / throughputPeriod, batchSize, batchThreadDelay, this, seed, true);
}

