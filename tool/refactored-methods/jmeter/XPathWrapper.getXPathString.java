/**
 * Not thread-safe - must be called from a synchronized method.
 *
 * @param file name of the file
 * @param xpathString xpath to look up in file
 * @return the next row from the file container
 */
public static String getXPathString(String file, String xpathString) {
    Map<String, XPathFileContainer> my = filePacks.get();
    String key = file + xpathString;
    XPathFileContainer xpfc = my.get(key);
    if (xpfc == null) {
        xpfc = getOrCreateContainer(key, file, xpathString);
        if (xpfc == null) {
            logErrorForNullContainer();
            //$NON-NLS-1$
            return "";
        }
        my.put(key, xpfc);
    }
    if (xpfc.size() == 0) {
        logWarningForEmptyContainer(file, xpathString);
        //$NON-NLS-1$
        return "";
    }
    int currentRow = xpfc.nextRow();
    if (log.isDebugEnabled()) {
        log.debug("getting match number {}", Integer.toString(currentRow));
    }
    return xpfc.getXPathString(currentRow);
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleException(Exception e) {
    log.warn(e.getLocalizedMessage());
}

private static XPathFileContainer getOrCreateContainer(String key, String file, String xpathString) {
    synchronized (fileContainers) {
        XPathFileContainer xpfc = fileContainers.get(key);
        if (xpfc == null) {
            xpfc = open(file, xpathString);
            if (xpfc != null) {
                fileContainers.put(key, xpfc);
            }
        }
        return xpfc;
    }
}

private static void logErrorForNullContainer() {
    log.error("XPathFileContainer is null!");
}

private static void logWarningForEmptyContainer(String file, String xpathString) {
    log.warn("XPathFileContainer has no nodes: {} {}", file, xpathString);
}

