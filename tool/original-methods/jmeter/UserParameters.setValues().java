@SuppressWarnings("SynchronizeOnNonFinalField")
private void setValues() {
    synchronized (lock) {
        if (log.isDebugEnabled()) {
            //$NON-NLS-1$
            log.debug("{} Running up named: {}", Thread.currentThread().getName(), getName());
        }
        PropertyIterator namesIter = getNames().iterator();
        PropertyIterator valueIter = getValues().iterator();
        JMeterVariables jmvars = getThreadContext().getVariables();
        while (namesIter.hasNext() && valueIter.hasNext()) {
            String name = namesIter.next().getStringValue();
            String value = valueIter.next().getStringValue();
            if (log.isDebugEnabled()) {
                //$NON-NLS-1$
                log.debug("{} saving variable: {}={}", Thread.currentThread().getName(), name, value);
            }
            jmvars.put(name, value);
        }
    }
}