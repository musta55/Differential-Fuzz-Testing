protected void internalRenderHead(IHeaderResponse response) {
    response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference())));
    super.renderHead(response);
    final String suffix = DEVELOPMENT == getApplication().getConfigurationType() ? "" : ".min";
    response.render(CssHeaderItem.forUrl("css/theme_om/jquery-ui" + suffix + ".css"));
    response.render(CssHeaderItem.forUrl("css/theme" + suffix + ".css"));
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
    response.render(CssHeaderItem.forReference(FontAwesome6CssReference.instance()));
    BootstrapResourcesBehavior.instance().renderHead(Bootstrap.getSettings(getApplication()), response);
    response.render(new FilteredHeaderItem(CssHeaderItem.forUrl("css/custom.css"), CUSTOM_CSS_FILTER));
}