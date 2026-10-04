@Override
public URLCollection load(Triple<String, URL, Charset> triple) throws Exception {
    final String cssContent = triple.getLeft();
    final URL baseUrl = triple.getMiddle();
    final Charset charset = triple.getRight();
    final CSSReaderSettings readerSettings = new CSSReaderSettings().setBrowserCompliantMode(true).setFallbackCharset(charset).setCSSVersion(ECSSVersion.CSS30).setCustomErrorHandler(new LoggingCSSParseErrorHandler()).setUseSourceLocation(false).setCustomExceptionHandler(new CSSParseExceptionCallback(baseUrl));
    if (IGNORE_ALL_CSS_ERRORS) {
        readerSettings.setInterpretErrorHandler(new DoNothingCSSInterpretErrorHandler());
    }
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader(cssContent, readerSettings);
    final URLCollection urls = new URLCollection(new ArrayList<>());
    if (aCSS == null) {
        LOG.warn("Failed parsing CSS: {}, got null CascadingStyleSheet", baseUrl);
        return urls;
    }
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
        public void onUrlDeclaration(final ICSSTopLevelRule aTopLevelRule, final CSSDeclaration aDeclaration, final CSSExpressionMemberTermURI aURITerm) {
            // NOOP
            // Browser fetch such urls only when CSS rule matches
            // so we disable this code
        }
    });
    return urls;
}