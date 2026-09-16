/**
 * {@inheritDoc}
 */
@Override
public long delay() {
    BSFManager mgr = null;
    try {
        mgr = getManager();
        Object result = evalFileOrScript(mgr);
        return parseDelay(result);
    } catch (NumberFormatException | BSFException e) {
        handleException(e);
        return 0;
    } finally {
        terminateManager(mgr);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static long parseDelay(Object result) {
    if (result == null) {
        log.warn("Script did not return a value");
        return 0;
    }
    return Long.parseLong(result.toString());
}

private static void handleException(Exception e) {
    if (log.isWarnEnabled()) {
        log.warn("Problem in BSF script. {}", e.toString());
    }
}

private static void terminateManager(BSFManager mgr) {
    if (mgr != null) {
        mgr.terminate();
    }
}

