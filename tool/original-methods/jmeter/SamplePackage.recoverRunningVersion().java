/**
 * Recover each member of SamplePackage to the state before the call of setRunningVersion(true)
 * @see TestElement#recoverRunningVersion()
 */
public void recoverRunningVersion() {
    recoverRunningVersion(configs);
    recoverRunningVersion(sampleListeners);
    recoverRunningVersion(assertions);
    recoverRunningVersion(timers);
    recoverRunningVersion(postProcessors);
    recoverRunningVersion(preProcessors);
    recoverRunningVersion(controllers);
    sampler.recoverRunningVersion();
}