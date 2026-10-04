@Override
public void handle(Class<?> service, String className, Throwable throwable) {
    if (throwable instanceof NoClassDefFoundError) {
        if (throwable.getMessage().contains("javafx")) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to load class {} for interface {}. The class depends on JavaFX which is missing in the current JVM. " + "Use JVM distribution with JavaFX, or add it as a library for the class to work", className, service, throwable);
            } else {
                log.info("Class {} depends on JavaFX which is missing in the current JVM. " + "Use JVM distribution with JavaFX, or add it as a library for the class to work", className);
            }
        } else {
            log.error("Exception registering implementation: [{}] of interface: [{}], a dependency used by the plugin class is missing", className, service, throwable);
        }
    } else {
        log.error("Exception registering implementation: [{}] of interface: [{}], a jar is probably missing", className, service, throwable);
    }
}