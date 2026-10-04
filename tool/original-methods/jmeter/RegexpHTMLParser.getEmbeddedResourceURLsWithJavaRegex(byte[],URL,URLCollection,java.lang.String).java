private static Iterator<URL> getEmbeddedResourceURLsWithJavaRegex(byte[] html, URL baseUrl, URLCollection urls, String encoding) throws HTMLParseException {
    try {
        // TODO: find a way to avoid the cost of creating a String here --
        // probably a new PatternMatcherInput working on a byte[] would do
        // better.
        String input = new String(html, encoding);
        Matcher matcher = HTML_PATTERN.matcher(input);
        while (matcher.find()) {
            java.util.regex.MatchResult match = matcher.toMatchResult();
            String s;
            if (log.isDebugEnabled()) {
                log.debug("match groups {} {}", match.groupCount(), match);
            }
            // Check for a BASE HREF:
            for (int g = 1; g <= NUM_BASE_GROUPS && g <= match.groupCount(); g++) {
                s = match.group(g);
                if (s != null) {
                    log.debug("new baseUrl: {} - {}", s, baseUrl);
                    try {
                        baseUrl = ConversionUtils.makeRelativeURL(baseUrl, s);
                    } catch (MalformedURLException e) {
                        // Doesn't even look like a URL?
                        // Maybe it isn't: Ignore the exception.
                        log.debug("Can't build base URL from URL {} in page {}", s, baseUrl, e);
                    }
                }
            }
            for (int g = NUM_BASE_GROUPS + 1; g <= match.groupCount(); g++) {
                s = match.group(g);
                if (s != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("group {} - {}", g, match.group(g));
                    }
                    urls.addURL(s, baseUrl);
                }
            }
        }
        return urls.iterator();
    } catch (UnsupportedEncodingException | PatternSyntaxException e) {
        throw new HTMLParseException(e.getMessage(), e);
    }
}