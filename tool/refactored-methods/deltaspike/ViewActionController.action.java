public void action() {
    printWindowId("ViewActionController#action");
}
// ---- helper method(s) introduced by the refactoring ----
private void printWindowId(String methodName) {
    FacesContext context = FacesContext.getCurrentInstance();
    System.out.println(methodName + " with windowId: " + clientWindow.getWindowId(context));
}

