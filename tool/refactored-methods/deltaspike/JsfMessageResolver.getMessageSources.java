@Override
protected List<String> getMessageSources(MessageContext messageContext) {
    List<String> result = new ArrayList<>(super.getMessageSources(messageContext));
    addBundleNames(result);
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private void addBundleNames(List<String> result) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext == null || facesContext.getCurrentPhaseId() == null) {
        return;
    }
    String bundleName = facesContext.getApplication().getMessageBundle();
    if (bundleName != null) {
        result.add(bundleName);
    }
    //default messages from jsf
    result.add(FacesMessage.FACES_MESSAGES);
}

