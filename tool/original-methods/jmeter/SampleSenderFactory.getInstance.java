/**
 * Checks for the JMeter property mode and returns the required class.
 *
 * @return the appropriate class. Standard JMeter functionality,
 * hold_samples until end of test or batch samples.
 */
static SampleSender getInstance(RemoteSampleListener listener) {
    // Extended property name
    // $NON-NLS-1$
    final String type = JMeterUtils.getPropDefault("mode", MODE_STRIPPED_BATCH);
    SampleSender s;
    if (type.equalsIgnoreCase(MODE_BATCH)) {
        s = new BatchSampleSender(listener);
    } else if (type.equalsIgnoreCase(MODE_STRIPPED_BATCH)) {
        s = new DataStrippingSampleSender(new BatchSampleSender(listener));
    } else if (type.equalsIgnoreCase(MODE_STATISTICAL)) {
        s = new StatisticalSampleSender(listener);
    } else if (type.equalsIgnoreCase(MODE_STANDARD)) {
        s = new StandardSampleSender(listener);
    } else if (type.equalsIgnoreCase(MODE_STRIPPED)) {
        s = new DataStrippingSampleSender(listener);
    } else if (type.equalsIgnoreCase(MODE_ASYNCH)) {
        s = new AsynchSampleSender(listener);
    } else if (type.equalsIgnoreCase(MODE_STRIPPED_ASYNCH)) {
        s = new DataStrippingSampleSender(new AsynchSampleSender(listener));
    } else if (type.equalsIgnoreCase(MODE_DISKSTORE)) {
        s = new DiskStoreSampleSender(listener);
    } else if (type.equalsIgnoreCase(MODE_STRIPPED_DISKSTORE)) {
        s = new DataStrippingSampleSender(new DiskStoreSampleSender(listener));
    } else {
        // should be a user provided class name
        try {
            Class<?> clazz = Class.forName(type);
            Constructor<?> cons = clazz.getConstructor(RemoteSampleListener.class);
            s = (SampleSender) cons.newInstance(listener);
        } catch (Exception e) {
            // houston we have a problem !!
            log.error("Unable to create a sample sender from class:'{}', search for " + "mode property in jmeter.properties for correct configuration options", type);
            throw new IllegalArgumentException("Unable to create a sample sender from mode or class:'" + type + "', search for mode property in jmeter.properties for correct configuration options, " + "message:" + e.getMessage(), e);
        }
    }
    return s;
}