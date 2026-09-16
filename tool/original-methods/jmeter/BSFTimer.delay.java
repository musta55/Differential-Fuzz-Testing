/**
 * {@inheritDoc}
 */
@Override
public long delay() {
    long delay = 0;
    BSFManager mgr = null;
    try {
        mgr = getManager();
        Object o = evalFileOrScript(mgr);
        if (o == null) {
            log.warn("Script did not return a value");
            return 0;
        }
        delay = Long.parseLong(o.toString());
    } catch (NumberFormatException | BSFException e) {
        if (log.isWarnEnabled()) {
            log.warn("Problem in BSF script. {}", e.toString());
        }
    } finally {
        if (mgr != null) {
            mgr.terminate();
        }
    }
    return delay;
}