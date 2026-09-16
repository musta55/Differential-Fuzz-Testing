public PreciseThroughputTimerBeanInfo() {
    super(PreciseThroughputTimer.class);
    createPropertyGroup(//$NON-NLS-1$
    "delay", new String[] { //$NON-NLS-1$
    "throughput", //$NON-NLS-1$
    "throughputPeriod", //$NON-NLS-1$
    "duration" });
    //$NON-NLS-1$
    setProperty("throughput", 100d);
    //$NON-NLS-1$
    setProperty("throughputPeriod", 3600);
    //$NON-NLS-1$
    setProperty("duration", 3600L);
    createPropertyGroup(//$NON-NLS-1$
    "batching", new String[] { "batchSize", "batchThreadDelay" });
    //$NON-NLS-1$
    setProperty("batchSize", 1);
    //$NON-NLS-1$
    setProperty("batchThreadDelay", 0);
    //$NON-NLS-1$
    setProperty("exactLimit", 10000, true);
    //$NON-NLS-1$
    setProperty("allowedThroughputSurplus", 1.0d, true);
    createPropertyGroup(//$NON-NLS-1$
    "repeatability", new String[] { //$NON-NLS-1$
    "randomSeed" });
    //$NON-NLS-1$
    setProperty("randomSeed", 0L);
}
// ---- helper method(s) introduced by the refactoring ----
private void setProperty(String propertyName, Object defaultValue) {
    setProperty(propertyName, defaultValue, false);
}

private void setProperty(String propertyName, Object defaultValue, boolean hidden) {
    //$NON-NLS-1$
    PropertyDescriptor p = property(propertyName);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, defaultValue);
    if (hidden) {
        p.setHidden(true);
    }
}

