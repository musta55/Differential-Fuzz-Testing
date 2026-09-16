@Override
public void requestInitialized(ServletRequestEvent sre) {
    if (activated) {
        /*
             * For some reason Tomcat seems to call requestInitialized() more than
             * once for a request. Not sure if this allowed according to the spec.
             */
        if (!RequestResponseHolder.REQUEST.isBound()) {
            RequestResponseHolder.REQUEST.bind(sre.getServletRequest());
        }
    }
}