@Produces
@Dependent
public ViewAccessContextManager getViewAccessContextManager() {
    return new InjectableViewAccessContextManager(this.deltaSpikeContextExtension.getViewAccessScopedContext());
}