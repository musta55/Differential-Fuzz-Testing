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
    sampler.setRunningVersion(running);
}