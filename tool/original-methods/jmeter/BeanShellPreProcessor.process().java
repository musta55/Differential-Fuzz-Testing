@Override
public void process() {
    final BeanShellInterpreter bshInterpreter = getBeanShellInterpreter();
    if (bshInterpreter == null) {
        log.error("BeanShell not found");
        return;
    }
    JMeterContext jmctx = JMeterContextService.getContext();
    Sampler sam = jmctx.getCurrentSampler();
    try {
        // Add variables for access to context and variables
        //$NON-NLS-1$
        bshInterpreter.set("sampler", sam);
        processFileOrScript(bshInterpreter);
    } catch (JMeterException e) {
        if (log.isWarnEnabled()) {
            log.warn("Problem in BeanShell script. {}", e.toString());
        }
    }
}