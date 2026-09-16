/**
 * Get current thread group using sampler's context
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterContext context;
    if (currentSampler != null) {
        context = currentSampler.getThreadContext();
    } else {
        context = JMeterContextService.getContext();
    }
    AbstractThreadGroup threadGroup = context.getThreadGroup();
    if (threadGroup != null) {
        return threadGroup.getName();
    } else {
        // Can happen if called from GUI or from non test threads
        return "";
    }
}