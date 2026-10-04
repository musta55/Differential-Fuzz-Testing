/**
 * Dumps the heap to the outputFile file in the same format as the hprof heap dump.
 * <p>
 * Calls the dumpHeap() method of the HotSpotDiagnostic MXBean, if available.
 * <p>
 * See
 * <a href="http://docs.oracle.com/javase/7/docs/jre/api/management/extension/com/sun/management/HotSpotDiagnosticMXBean.html">
 * HotSpotDiagnosticMXBean
 * </a>
 * @param fileName name of the heap dump file. Must be creatable, i.e. must not exist.
 * @param live if true, dump only the live objects
 * @throws Exception if the MXBean cannot be found, or if there is a problem during invocation
 */
public static void dumpHeap(String fileName, boolean live) throws Exception {
    getInstance().performHeapDump(fileName, live);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Perform the dump using the dumpHeap method.
 *
 * @param fileName the file to use
 * @param live true to dump only live objects
 * @throws Exception if the MXBean cannot be found, or if there is a problem during invocation
 */
private void performHeapDump(String fileName, boolean live) throws Exception {
    if (exception != null) {
        throw exception;
    }
    try {
        server.invoke(hotspotDiagnosticBean, "dumpHeap", new Object[] { fileName, live }, new String[] { "java.lang.String", "boolean" });
    } catch (RuntimeMBeanException | MBeanException e) {
        Throwable cause = e.getCause();
        if (cause instanceof Exception) {
            throw (Exception) cause;
        }
        throw e;
    }
}

