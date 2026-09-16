@Override
public Locale getLocale() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext == null || facesContext.getCurrentPhaseId() == null) {
        return super.getLocale();
    }
    UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot != null) {
        Locale result = viewRoot.getLocale();
        if (isSupportedLocale(facesContext, result)) {
            return result;
        }
    }
    Locale defaultLocale = facesContext.getApplication().getDefaultLocale();
    return defaultLocale != null ? defaultLocale : super.getLocale();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSupportedLocale(FacesContext facesContext, Locale locale) {
    if (locale == null) {
        return false;
    }
    Iterator<Locale> supportedLocales = facesContext.getApplication().getSupportedLocales();
    while (supportedLocales.hasNext()) {
        if (locale.equals(supportedLocales.next())) {
            return true;
        }
    }
    return false;
}

