@Produces
@Dependent
public JmxBroadcaster jmxBroadcaster(final InjectionPoint ip) {
    final Class<?> declaringClass = ip.getMember().getDeclaringClass();
    final JmxBroadcaster broadcaster = extension.getBroadcasterFor(declaringClass);
    if (broadcaster == null) {
        //TODO discuss validation during bootstrapping
        throw new IllegalStateException(createExceptionMessage(declaringClass));
    }
    return broadcaster;
}
// ---- helper method(s) introduced by the refactoring ----
private String createExceptionMessage(Class<?> declaringClass) {
    return "Invalid injection of " + JmxBroadcaster.class.getName() + " in " + declaringClass.getName() + " detected. It is required to annotate the class with @" + MBean.class.getName();
}

