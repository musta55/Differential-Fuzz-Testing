public void linkAction() {
    FacesContext context = FacesContext.getCurrentInstance();
    System.out.println("ViewActionController#linkAction with windowId: " + clientWindow.getWindowId(context));
    lastTimeLinkAction = new Date();
}