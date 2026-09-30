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
    L launcher;
    // If the static method for creating a new instance is present in the launcher, it is invoked to create an instance.
    // This gives an opportunity for the launcher to do something custom when creating an instance. If the method is not
    // present, the service is loaded from the class name. A factory approach would be cleaner and type safe but adds
    // unnecessary complexity, going with the static method for now.
    try {
        Method m = launchMode.clazz.getDeclaredMethod(NEW_INSTANCE_METHOD);
        launcher = (L) m.invoke(null);
    } catch (NoSuchMethodException e) {
        launcher = loadService(launchMode.clazz);
    } catch (InvocationTargetException | IllegalAccessException e) {
        throw Throwables.propagate(e);
    }
    return launcher;
}