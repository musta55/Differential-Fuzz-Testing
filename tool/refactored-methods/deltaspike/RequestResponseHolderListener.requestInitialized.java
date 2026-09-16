@Override
public void requestInitialized(ServletRequestEvent sre) {
    if (isActivated()) {
        /*
             * For some reason Tomcat seems to call requestInitialized() more than
             * once for a request. Not sure if this allowed according to the spec.
             */
        if (!RequestResponseHolder.REQUEST.isBound()) {
            RequestResponseHolder.REQUEST.bind(sre.getServletRequest());
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isActivated() {
    return activated;
}

