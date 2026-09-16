/**
 * Checks for the JMeter property mode and returns the required class.
 *
 * @return the appropriate class. Standard JMeter functionality,
 * hold_samples until end of test or batch samples.
 */
static SampleSender getInstance(RemoteSampleListener listener) {
    // Extended property name
    final String type = JMeterUtils.getPropDefault("mode", "StrippedBatch");
    SampleSenderCreator creator = MODE_MAP.get(type);
    if (creator != null) {
        return creator.create(listener);
    } else {
        // should be a user provided class name
        try {
            Class<?> clazz = Class.forName(type);
            Constructor<?> cons = clazz.getConstructor(RemoteSampleListener.class);
            return (SampleSender) cons.newInstance(listener);
        } catch (Exception e) {
            // houston we have a problem !!
            log.error("Unable to create a sample sender from class:'{}', search for " + "mode property in jmeter.properties for correct configuration options", type);
            throw new IllegalArgumentException("Unable to create a sample sender from mode or class:'" + type + "', search for mode property in jmeter.properties for correct configuration options, " + "message:" + e.getMessage(), e);
        }
    }
}