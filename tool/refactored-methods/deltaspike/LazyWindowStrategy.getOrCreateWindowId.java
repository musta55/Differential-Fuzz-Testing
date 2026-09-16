@Override
protected String getOrCreateWindowId(FacesContext facesContext) {
    String windowId = ClientWindowHelper.getInitialRedirectWindowId(facesContext);
    if (StringUtils.isEmpty(windowId)) {
        windowId = getWindowIdParameter(facesContext);
    }
    if (StringUtils.isEmpty(windowId) && isPost(facesContext)) {
        windowId = getWindowIdPostParameter(facesContext);
    }
    if (StringUtils.isEmpty(windowId)) {
        handleInitialRedirectOrGenerateNewWindowId(facesContext);
    }
    return windowId;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleInitialRedirectOrGenerateNewWindowId(FacesContext facesContext) {
    if (jsfModuleConfig.isInitialRedirectEnabled() && !isPost(facesContext)) {
        ClientWindowHelper.handleInitialRedirect(facesContext, generateNewWindowId());
        facesContext.responseComplete();
    } else {
        generateAndSetNewWindowId();
    }
}

private void generateAndSetNewWindowId() {
    generateNewWindowId();
}

