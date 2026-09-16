/**
 * Constructor for wrapping the given {@link LifecycleFactory}
 *
 * @param wrapped lifecycle-factory which should be wrapped
 */
public DeltaSpikeLifecycleFactoryWrapper(LifecycleFactory wrapped) {
    this.wrapped = wrapped;
    this.deactivated = !ClassDeactivationUtils.isActivated(getClass());
    this.jsfVersionWithClientWindowDetected = detectClientWindowSupport();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean detectClientWindowSupport() {
    boolean jsfVersionWithClientWindowDetected = ClassUtils.tryToLoadClassForName(JsfModuleConfig.CLIENT_WINDOW_CLASS_NAME) != null;
    if (jsfVersionWithClientWindowDetected && ClassUtils.tryToLoadClassForName("org.apache.deltaspike.jsf.impl.listener.request.JsfClientWindowAwareLifecycleWrapper") == null) {
        jsfVersionWithClientWindowDetected = false;
        JsfUtils.logWrongModuleUsage(getClass().getName());
    }
    return jsfVersionWithClientWindowDetected;
}

private Lifecycle createLifecycleWrapper(Lifecycle result) {
    if (this.jsfVersionWithClientWindowDetected) {
        Class<? extends Lifecycle> lifecycleWrapperClass = ClassUtils.tryToLoadClassForName("org.apache.deltaspike.jsf.impl.listener.request.JsfClientWindowAwareLifecycleWrapper");
        try {
            return (Lifecycle) lifecycleWrapperClass.getConstructor(new Class[] { Lifecycle.class }).newInstance(new DeltaSpikeLifecycleWrapper(result));
        } catch (Exception e) {
            throw ExceptionUtils.throwAsRuntimeException(e);
        }
    }
    return new DeltaSpikeLifecycleWrapper(result);
}

