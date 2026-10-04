private static XPathFileContainer open(String file, String xpathString) {
    if (log.isInfoEnabled()) {
        log.info("{}: Opening {}", Thread.currentThread().getName(), file);
    }
    try {
        return new XPathFileContainer(file, xpathString);
    } catch (Exception e) {
        handleException(e);
        return null;
    }
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

