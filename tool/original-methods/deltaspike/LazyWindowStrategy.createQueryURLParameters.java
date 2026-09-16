@Override
protected Map<String, String> createQueryURLParameters(FacesContext facesContext) {
    String windowId = getWindowId(facesContext);
    if (windowId == null) {
        return null;
    }
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(ClientWindowHelper.RequestParameters.GET_WINDOW_ID, windowId);
    return parameters;
}