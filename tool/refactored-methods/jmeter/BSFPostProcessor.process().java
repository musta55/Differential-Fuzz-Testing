@Override
public void process() {
    BSFManager mgr = null;
    try {
        mgr = getManager();
        processScript(mgr);
    } catch (BSFException e) {
        logScriptError(e);
    } finally {
        if (mgr != null) {
            mgr.terminate();
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void processScript(BSFManager mgr) throws BSFException {
    processFileOrScript(mgr);
}

private static void logScriptError(BSFException e) {
    if (log.isWarnEnabled()) {
        log.warn("Problem in BSF script: {}", e.toString());
    }
}

