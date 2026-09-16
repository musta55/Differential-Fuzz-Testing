@Override
public URLCollection load(Triple<String, URL, Charset> triple) throws Exception {
    final String cssContent = triple.getLeft();
    final URL baseUrl = triple.getMiddle();
    final Charset charset = triple.getRight();
    final CascadingStyleSheet aCSS = parseCss(cssContent, baseUrl, charset);
    final URLCollection urls = new URLCollection(new ArrayList<>());
    if (aCSS == null) {
        LOG.warn("Failed parsing CSS: {}, got null CascadingStyleSheet", baseUrl);
        return urls;
    }
    collectUrls(aCSS, baseUrl, urls);
    return urls;
}
// ---- helper method(s) introduced by the refactoring ----
private static CascadingStyleSheet parseCss(String cssContent, URL baseUrl, Charset charset) {
    final CSSReaderSettings readerSettings = new CSSReaderSettings().setBrowserCompliantMode(true).setFallbackCharset(charset).setCSSVersion(ECSSVersion.CSS30).setCustomErrorHandler(new LoggingCSSParseErrorHandler()).setUseSourceLocation(false).setCustomExceptionHandler(new CSSParseExceptionCallback(baseUrl));
    if (IGNORE_ALL_CSS_ERRORS) {
        readerSettings.setInterpretErrorHandler(new DoNothingCSSInterpretErrorHandler());
    }
    return CSSReader.readFromStringReader(cssContent, readerSettings);
}

private static void collectUrls(CascadingStyleSheet aCSS, URL baseUrl, URLCollection urls) {
    CSSVisitor.visitCSSUrl(aCSS, new DefaultCSSUrlVisitor() {

        @Override
        public void onImport(CSSImportRule rule) {
            final String location = rule.getLocationString();
            if (!StringUtils.isEmpty(location)) {
                urls.addURL(location, baseUrl);
            }
        }

        // Call for URLs outside of URLs
        @Override
        public void onUrlDeclaration(ICSSTopLevelRule aTopLevelRule, CSSDeclaration aDeclaration, CSSExpressionMemberTermURI aURITerm) {
            // NOOP
            // Browser fetch such urls only when CSS rule matches
            // so we disable this code
        }
    });
}

