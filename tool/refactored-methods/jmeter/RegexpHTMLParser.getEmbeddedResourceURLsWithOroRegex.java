private static Iterator<URL> getEmbeddedResourceURLsWithOroRegex(byte[] html, URL baseUrl, URLCollection urls, String encoding) throws HTMLParseException {
    Pattern pattern = null;
    Perl5Matcher matcher = null;
    try {
        matcher = JMeterUtils.getMatcher();
        PatternMatcherInput input = localInput.get();
        input.setInput(new String(html, encoding));
        pattern = JMeterUtils.getPatternCache().getPattern(REGEXP, Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.SINGLELINE_MASK | Perl5Compiler.READ_ONLY_MASK);
        while (matcher.contains(input, pattern)) {
            MatchResult match = matcher.getMatch();
            processBaseHref(match, baseUrl);
            processUrls(match, baseUrl, urls);
        }
        return urls.iterator();
    } catch (UnsupportedEncodingException | MalformedCachePatternException e) {
        throw new HTMLParseException(e.getMessage(), e);
    } finally {
        JMeterUtils.clearMatcherMemory(matcher, pattern);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void processBaseHref(java.util.regex.MatchResult match, URL baseUrl) {
    for (int g = 1; g <= NUM_BASE_GROUPS && g <= match.groupCount(); g++) {
        String s = match.group(g);
        if (s != null) {
            log.debug("new baseUrl: {} - {}", s, baseUrl);
            try {
                baseUrl = ConversionUtils.makeRelativeURL(baseUrl, s);
            } catch (MalformedURLException e) {
                log.debug("Can't build base URL from URL {} in page {}", s, baseUrl, e);
            }
        }
    }
}

private static void processUrls(java.util.regex.MatchResult match, URL baseUrl, URLCollection urls) {
    for (int g = NUM_BASE_GROUPS + 1; g <= match.groupCount(); g++) {
        String s = match.group(g);
        if (s != null) {
            log.debug("group {} - {}", g, match.group(g));
            urls.addURL(s, baseUrl);
        }
    }
}

private static void processBaseHref(MatchResult match, URL baseUrl) {
    for (int g = 1; g <= NUM_BASE_GROUPS && g <= match.groups(); g++) {
        String s = match.group(g);
        if (s != null) {
            log.debug("new baseUrl: {} - {}", s, baseUrl);
            try {
                baseUrl = ConversionUtils.makeRelativeURL(baseUrl, s);
            } catch (MalformedURLException e) {
                log.debug("Can't build base URL from URL {} in page {}", s, baseUrl, e);
            }
        }
    }
}

private static void processUrls(MatchResult match, URL baseUrl, URLCollection urls) {
    for (int g = NUM_BASE_GROUPS + 1; g <= match.groups(); g++) {
        String s = match.group(g);
        if (s != null) {
            log.debug("group {} - {}", g, match.group(g));
            urls.addURL(s, baseUrl);
        }
    }
}

