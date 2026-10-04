private static void addFormUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config, List<? super HTTPSamplerBase> potentialLinks) {
    List<HTTPSamplerBase> urls = createUrlsFromForms(html, result.getURL());
    for (HTTPSamplerBase newUrl : urls) {
        newUrl.setMethod(HTTPConstants.POST);
        if (log.isDebugEnabled()) {
            log.debug("Potential Form match: " + newUrl.toString());
        }
        if (HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
            log.debug("Matched!");
            potentialLinks.add(newUrl);
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void extractAndAddUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config, List<? super HTTPSamplerBase> potentialLinks) {
    addAnchorUrls(html, result, config, potentialLinks);
    addFormUrls(html, result, config, potentialLinks);
    addFramesetUrls(html, result, config, potentialLinks);
}

private static List<HTTPSamplerBase> createUrlsFromForms(Document html, java.net.URL baseUrl) {
    NodeList rootList = html.getChildNodes();
    List<HTTPSamplerBase> urls = new ArrayList<>();
    for (int x = 0; x < rootList.getLength(); x++) {
        urls.addAll(HtmlParsingUtils.createURLFromForm(rootList.item(x), baseUrl));
    }
    return urls;
}

private static String getBaseUrl(Document html) {
    String base = "";
    // $NON-NLS-1$
    NodeList baseList = html.getElementsByTagName("base");
    if (baseList.getLength() > 0) {
        // $NON-NLS-1$
        base = baseList.item(0).getAttributes().getNamedItem("href").getNodeValue();
    }
    return base;
}

