@Override
public String getId() {
    return this.window.getWindowId(FacesContext.getCurrentInstance());
}