/**
 * Constructor for wrapping the given {@link LifecycleFactory}
 *
 * @param wrapped lifecycle-factory which should be wrapped
 */
public DeltaSpikeLifecycleFactoryWrapper(LifecycleFactory wrapped) {
    this.wrapped = wrapped;
    this.deactivated = !ClassDeactivationUtils.isActivated(getClass());
    boolean jsfVersionWithClientWindowDetected = ClassUtils.tryToLoadClassForName(JsfModuleConfig.CLIENT_WINDOW_CLASS_NAME) != null;
    if (jsfVersionWithClientWindowDetected && ClassUtils.tryToLoadClassForName("org.apache.deltaspike.jsf.impl.listener.request.JsfClientWindowAwareLifecycleWrapper") == null) {
        jsfVersionWithClientWindowDetected = false;
        JsfUtils.logWrongModuleUsage(getClass().getName());
    }
    this.jsfVersionWithClientWindowDetected = jsfVersionWithClientWindowDetected;
}