/**
 * Make the SamplePackage the running version, or make it no longer the
 * running version. This tells to each element of the SamplePackage that it's current state must
 * be retrievable by a call to recoverRunningVersion().
 * @param running boolean
 * @see TestElement#setRunningVersion(boolean)
 */
public void setRunningVersion(boolean running) {
    setRunningVersion(configs, running);
    setRunningVersion(sampleListeners, running);
    setRunningVersion(assertions, running);
    setRunningVersion(timers, running);
    setRunningVersion(postProcessors, running);
    setRunningVersion(preProcessors, running);
    setRunningVersion(controllers, running);
    if (sampler != null) {
        sampler.setRunningVersion(running);
    }
}
// ---- helper method(s) introduced by the refactoring ----
public SamplePackage() {
}

public SamplePackage(List<ConfigTestElement> configs) {
    this.configs.addAll(configs);
}

public SamplePackage(List<ConfigTestElement> configs, List<SampleListener> listeners) {
    this(configs);
    this.sampleListeners.addAll(listeners);
}

public SamplePackage(List<ConfigTestElement> configs, List<SampleListener> listeners, List<Timer> timers) {
    this(configs, listeners);
    this.timers.addAll(timers);
}

public SamplePackage(List<ConfigTestElement> configs, List<SampleListener> listeners, List<Timer> timers, List<Assertion> assertions) {
    this(configs, listeners, timers);
    this.assertions.addAll(assertions);
}

public SamplePackage(List<ConfigTestElement> configs, List<SampleListener> listeners, List<Timer> timers, List<Assertion> assertions, List<PostProcessor> postProcessors) {
    this(configs, listeners, timers, assertions);
    this.postProcessors.addAll(postProcessors);
}

public SamplePackage(List<ConfigTestElement> configs, List<SampleListener> listeners, List<Timer> timers, List<Assertion> assertions, List<PostProcessor> postProcessors, List<PreProcessor> preProcessors) {
    this(configs, listeners, timers, assertions, postProcessors);
    this.preProcessors.addAll(preProcessors);
}

