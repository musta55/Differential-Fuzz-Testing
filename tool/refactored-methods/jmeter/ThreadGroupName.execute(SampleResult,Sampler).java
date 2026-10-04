/**
 * Get current thread group using sampler's context
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterContext context = getContext(currentSampler);
    AbstractThreadGroup threadGroup = context.getThreadGroup();
    if (threadGroup != null) {
        return threadGroup.getName();
    } else {
        // Can happen if called from GUI or from non test threads
        return "";
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static JMeterContext getContext(Sampler currentSampler) {
    if (currentSampler != null) {
        return currentSampler.getThreadContext();
    } else {
        return JMeterContextService.getContext();
    }
}

