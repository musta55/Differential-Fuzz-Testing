/**
 * Returns list of URLs to add to the thread's classloader.
 * @param urls - list of URLs to add to the thread's classloader
 */
public static void updateLoader(URL[] urls) {
    DynamicClassLoader loader = (DynamicClassLoader) Thread.currentThread().getContextClassLoader();
    for (URL url : urls) {
        loader.addURL(url);
    }
}