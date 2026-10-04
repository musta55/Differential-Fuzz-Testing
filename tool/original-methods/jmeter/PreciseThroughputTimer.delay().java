@Override
public long delay() {
    double nextEvent;
    EventProducer events = getEventProducer();
    synchronized (events) {
        nextEvent = events.next();
    }
    long now = System.currentTimeMillis();
    long testStarted = JMeterContextService.getTestStartTime();
    long delay = (long) (nextEvent * TimeUnit.SECONDS.toMillis(1) + testStarted - now);
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