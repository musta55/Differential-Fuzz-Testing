@Override
protected List<String> getMessageSources(MessageContext messageContext) {
    List<String> result = new ArrayList<String>(super.getMessageSources(messageContext));
    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext == null || facesContext.getCurrentPhaseId() == null) {
        return result;
    }
    String bundleName = facesContext.getApplication().getMessageBundle();
    if (bundleName != null) {
        result.add(bundleName);
    }
    //default messages from jsf
    result.add(FacesMessage.FACES_MESSAGES);
    return result;
}