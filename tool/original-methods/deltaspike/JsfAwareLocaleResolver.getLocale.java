@Override
public Locale getLocale() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext != null && facesContext.getCurrentPhaseId() != null) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Locale result = null;
        if (viewRoot != null) {
            // if a ViewRoot is present we return the Locale from there
            result = viewRoot.getLocale();
        }
        if (result != null) {
            Iterator<Locale> supportedLocale = facesContext.getApplication().getSupportedLocales();
            boolean supportedLocaleConfigured = false;
            while (supportedLocale.hasNext()) {
                supportedLocaleConfigured = true;
                if (result.equals(supportedLocale.next())) {
                    return result;
                }
            }
            if (!supportedLocaleConfigured) {
                return result;
            }
        }
        result = facesContext.getApplication().getDefaultLocale();
        if (result != null) {
            return result;
        }
    }
    // return the default Locale, if no Locale was found
    return super.getLocale();
}