public SamplePackage(List<ConfigTestElement> configs, List<SampleListener> listeners, List<Timer> timers, List<Assertion> assertions, List<PostProcessor> postProcessors, List<PreProcessor> preProcessors, List<Controller> controllers) {
    this.configs = configs;
    this.sampleListeners = listeners;
    this.timers = timers;
    this.assertions = assertions;
    this.postProcessors = postProcessors;
    this.preProcessors = preProcessors;
    this.controllers = controllers;
}