protected BasePage() {
    checkInitializationStatus();
    initOptions();
}
// ---- helper method(s) introduced by the refactoring ----
private void checkInitializationStatus() {
    if (isInitComplete()) {
        if (!isInstalled() && !(this instanceof InstallWizardPage)) {
            throw new RestartResponseException(InstallWizardPage.class);
        }
    } else if (!(this instanceof NotInitedPage)) {
        throw new RestartResponseException(NotInitedPage.class);
    }
}

private void initOptions() {
    options.put("fragmentIdentifierSuffix", "");
    options.put("keyValueDelimiter", "/");
}

private void renderJQuery(IHeaderResponse response) {
    response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference())));
}

private void renderThemeStyles(IHeaderResponse response) {
    String suffix = DEVELOPMENT == getApplication().getConfigurationType() ? "" : ".min";
    response.render(CssHeaderItem.forUrl("css/theme_om/jquery-ui" + suffix + ".css"));
    response.render(CssHeaderItem.forUrl("css/theme" + suffix + ".css"));
}

private void renderGoogleAnalytics(IHeaderResponse response) {
    if (!Strings.isEmpty(getGaCode())) {
        response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(BasePage.class, "om-ga.js") {

            private static final long serialVersionUID = 1L;

            @Override
            public List<HeaderItem> getDependencies() {
                return List.of(new PriorityHeaderItem(JavaScriptHeaderItem.forUrl("https://www.googletagmanager.com/gtag/js?id=" + getGaCode()).setAsync(true)));
            }
        })));
        StringBuilder script = new StringBuilder("initGA('").append(getGaCode()).append("', ").append(isMainPage()).append(");");
        response.render(OnDomReadyHeaderItem.forScript(script));
    }
}

private void renderFontAwesome(IHeaderResponse response) {
    response.render(CssHeaderItem.forReference(FontAwesome6CssReference.instance()));
}

private void renderBootstrapResources(IHeaderResponse response) {
    BootstrapResourcesBehavior.instance().renderHead(Bootstrap.getSettings(getApplication()), response);
}

private void renderCustomStyles(IHeaderResponse response) {
    response.render(new FilteredHeaderItem(CssHeaderItem.forUrl("css/custom.css"), CUSTOM_CSS_FILTER));
}

