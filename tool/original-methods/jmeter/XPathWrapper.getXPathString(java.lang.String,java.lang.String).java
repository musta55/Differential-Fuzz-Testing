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
    if (// We don't have a local copy
    xpfc == null) {
        synchronized (fileContainers) {
            xpfc = fileContainers.get(key);
            if (xpfc == null) {
                // There's no global copy either
                xpfc = open(file, xpathString);
            }
            if (xpfc != null) {
                // save the global copy
                fileContainers.put(key, xpfc);
            }
        }
        // TODO improve the error handling
        if (xpfc == null) {
            log.error("XPathFileContainer is null!");
            //$NON-NLS-1$
            return "";
        }
        // save our local copy
        my.put(key, xpfc);
    }
    if (xpfc.size() == 0) {
        log.warn("XPathFileContainer has no nodes: {} {}", file, xpathString);
        //$NON-NLS-1$
        return "";
    }
    int currentRow = xpfc.nextRow();
    if (log.isDebugEnabled()) {
        log.debug("getting match number {}", Integer.toString(currentRow));
    }
    return xpfc.getXPathString(currentRow);
}