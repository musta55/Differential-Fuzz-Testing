public PreciseThroughputTimerBeanInfo() {
    super(PreciseThroughputTimer.class);
    createPropertyGroup(//$NON-NLS-1$
    "delay", new String[] { //$NON-NLS-1$
    "throughput", //$NON-NLS-1$
    "throughputPeriod", //$NON-NLS-1$
    "duration" });
    PropertyDescriptor p;
    //$NON-NLS-1$
    p = property("throughput");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 100d);
    //$NON-NLS-1$
    p = property("throughputPeriod");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 3600);
    //$NON-NLS-1$
    p = property("duration");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 3600L);
    createPropertyGroup(//$NON-NLS-1$
    "batching", new String[] { "batchSize", "batchThreadDelay" });
    //$NON-NLS-1$
    p = property("batchSize");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 1);
    //$NON-NLS-1$
    p = property("batchThreadDelay");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 0);
    //$NON-NLS-1$
    p = property("exactLimit");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 10000);
    p.setHidden(true);
    //$NON-NLS-1$
    p = property("allowedThroughputSurplus");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 1.0d);
    p.setHidden(true);
    createPropertyGroup(//$NON-NLS-1$
    "repeatability", new String[] { //$NON-NLS-1$
    "randomSeed" });
    //$NON-NLS-1$
    p = property("randomSeed");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, 0L);
}