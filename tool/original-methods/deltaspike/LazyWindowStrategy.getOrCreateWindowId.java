@Override
protected String getOrCreateWindowId(FacesContext facesContext) {
    String windowId = ClientWindowHelper.getInitialRedirectWindowId(facesContext);
    if (StringUtils.isEmpty(windowId)) {
        windowId = getWindowIdParameter(facesContext);
    }
    boolean post = isPost(facesContext);
    if (StringUtils.isEmpty(windowId) && post) {
        windowId = getWindowIdPostParameter(facesContext);
    }
    if (StringUtils.isEmpty(windowId)) {
        if (jsfModuleConfig.isInitialRedirectEnabled() && !post) {
            ClientWindowHelper.handleInitialRedirect(facesContext, generateNewWindowId());
            facesContext.responseComplete();
            windowId = null;
        } else {
            windowId = generateNewWindowId();
        }
    }
    return windowId;
}