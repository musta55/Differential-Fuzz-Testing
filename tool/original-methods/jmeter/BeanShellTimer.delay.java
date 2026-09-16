/**
 * {@inheritDoc}
 */
@Override
public long delay() {
    String ret = "0";
    final BeanShellInterpreter bshInterpreter = getBeanShellInterpreter();
    if (bshInterpreter == null) {
        log.error("BeanShell not found");
        return 0;
    }
    try {
        Object o = processFileOrScript(bshInterpreter);
        if (o != null) {
            ret = o.toString();
        }
    } catch (JMeterException e) {
        if (log.isWarnEnabled()) {
            log.warn("Problem in BeanShell script. {}", e.toString());
        }
    }
    try {
        return Long.decode(ret);
    } catch (NumberFormatException e) {
        log.warn("Number format exception while decoding number: '{}'", ret);
        return 0;
    }
}