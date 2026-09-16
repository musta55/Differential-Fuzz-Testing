@Override
public Lifecycle getLifecycle(String s) {
    Lifecycle result = this.wrapped.getLifecycle(s);
    if (this.deactivated) {
        return result;
    }
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