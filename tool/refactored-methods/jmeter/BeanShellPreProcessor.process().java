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
        setupInterpreter(bshInterpreter, sam);
        processFileOrScript(bshInterpreter);
    } catch (JMeterException e) {
        if (log.isWarnEnabled()) {
            log.warn("Problem in BeanShell script. {}", e.toString());
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void setupInterpreter(BeanShellInterpreter bshInterpreter, Sampler sam) throws JMeterException {
    //$NON-NLS-1$
    bshInterpreter.set("sampler", sam);
}

