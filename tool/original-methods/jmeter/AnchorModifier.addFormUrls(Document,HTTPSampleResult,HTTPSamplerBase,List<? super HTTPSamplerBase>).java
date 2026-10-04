private static void addFormUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config, List<? super HTTPSamplerBase> potentialLinks) {
    NodeList rootList = html.getChildNodes();
    List<HTTPSamplerBase> urls = new ArrayList<>();
    for (int x = 0; x < rootList.getLength(); x++) {
        urls.addAll(HtmlParsingUtils.createURLFromForm(rootList.item(x), result.getURL()));
    }
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