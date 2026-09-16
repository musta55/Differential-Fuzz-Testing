@Produces
@Dependent
public ViewAccessContextManager getViewAccessContextManager() {
    return new InjectableViewAccessContextManager(deltaSpikeContextExtension.getViewAccessScopedContext());
}