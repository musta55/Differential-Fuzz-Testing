@Override
public void process() {
    BSFManager mgr = createManager();
    if (mgr == null) {
        return;
    }
    try {
        processFileOrScript(mgr);
    } catch (BSFException e) {
        if (log.isWarnEnabled()) {
            log.warn("Problem in BSF script. {}", e.toString());
        }
    } finally {
        terminateManager(mgr);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private BSFManager createManager() {
    try {
        return getManager();
    } catch (Exception e) {
        log.error("Failed to create BSFManager", e);
        return null;
    }
}

private static void terminateManager(BSFManager mgr) {
    if (mgr != null) {
        mgr.terminate();
    }
}

