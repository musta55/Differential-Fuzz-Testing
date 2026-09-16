@Override
protected Map<String, String> createQueryURLParameters(FacesContext facesContext) {
    String windowId = getWindowId(facesContext);
    if (windowId == null) {
        return null;
    }
    Map<String, String> parameters = new HashMap<>();
    parameters.put(ClientWindowHelper.RequestParameters.GET_WINDOW_ID, windowId);
    return parameters;
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

