@Override
public void requestDestroyed(ServletRequestEvent sre) {
    if (isActivated()) {
        RequestResponseHolder.REQUEST.release();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isActivated() {
    return activated;
}

