@Override
public void process() {
    BSFManager mgr = null;
    try {
        mgr = getManager();
        processFileOrScript(mgr);
    } catch (BSFException e) {
        if (log.isWarnEnabled()) {
            log.warn("Problem in BSF script: {}", e.toString());
        }
    } finally {
        if (mgr != null) {
            mgr.terminate();
        }
    }
}