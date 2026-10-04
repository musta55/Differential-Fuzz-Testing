public SamplePackage(List<ConfigTestElement> configs, List<SampleListener> listeners, List<Timer> timers, List<Assertion> assertions, List<PostProcessor> postProcessors, List<PreProcessor> preProcessors, List<Controller> controllers) {
    this(configs, listeners, timers, assertions, postProcessors, preProcessors);
    this.controllers.addAll(controllers);
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

