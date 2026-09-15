/**
 * Get a launcher instance.<br><br>
 *
 * Returns a launcher specific to the given launch mode. This allows the user to also use custom methods supported by
 * the specific launcher along with the basic launch methods from this class.
 *
 * @param launchMode - The launch mode to use
 *
 * @return The launcher
 */
public static <L extends Launcher<?>> L getLauncher(LaunchMode<L> launchMode) {
    try {
        return invokeNewInstanceMethod(launchMode);
    } catch (NoSuchMethodException e) {
        return loadService(launchMode.clazz);
    } catch (InvocationTargetException | IllegalAccessException e) {
        throw rethrowException(e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static <L extends Launcher<?>> L invokeNewInstanceMethod(LaunchMode<L> launchMode) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = launchMode.clazz.getDeclaredMethod(NEW_INSTANCE_METHOD);
    return (L) method.invoke(null);
}

private static RuntimeException rethrowException(Throwable throwable) {
    return Throwables.propagate(throwable);
}

