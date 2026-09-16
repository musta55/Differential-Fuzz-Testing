public void action() {
    FacesContext context = FacesContext.getCurrentInstance();
    System.out.println("ViewActionController#action with windowId: " + clientWindow.getWindowId(context));
}