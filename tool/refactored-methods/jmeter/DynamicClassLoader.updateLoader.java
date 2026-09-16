/**
 * Updates the current thread's classloader with the provided URLs.
 * @param urls - list of URLs to add to the thread's classloader
 */
public static void updateLoader(URL[] urls) {
    DynamicClassLoader loader = getCurrentThreadClassLoader();
    addUrlsToLoader(loader, urls);
}
// ---- helper method(s) introduced by the refactoring ----
private static DynamicClassLoader getCurrentThreadClassLoader() {
    return (DynamicClassLoader) Thread.currentThread().getContextClassLoader();
}

private static void addUrlsToLoader(DynamicClassLoader loader, URL[] urls) {
    for (URL url : urls) {
        loader.addURL(url);
    }
}

