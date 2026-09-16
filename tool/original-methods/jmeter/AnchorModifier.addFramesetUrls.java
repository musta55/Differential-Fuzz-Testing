private static void addFramesetUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config, List<? super HTTPSamplerBase> potentialLinks) {
    String base = "";
    // $NON-NLS-1$
    NodeList baseList = html.getElementsByTagName("base");
    if (baseList.getLength() > 0) {
        base = // $NON-NLS-1$
        baseList.item(0).getAttributes().getNamedItem("href").getNodeValue();
    }
    // $NON-NLS-1$
    NodeList nodeList = html.getElementsByTagName("frame");
    for (int i = 0; i < nodeList.getLength(); i++) {
        Node tempNode = nodeList.item(i);
        NamedNodeMap nnm = tempNode.getAttributes();
        // $NON-NLS-1$
        Node namedItem = nnm.getNamedItem("src");
        if (namedItem == null) {
            continue;
        }
        String hrefStr = namedItem.getNodeValue();
        try {
            HTTPSamplerBase newUrl = HtmlParsingUtils.createUrlFromAnchor(hrefStr, ConversionUtils.makeRelativeURL(result.getURL(), base));
            newUrl.setMethod(HTTPConstants.GET);
            if (log.isDebugEnabled()) {
                log.debug("Potential <frame src> match: " + newUrl);
            }
            if (HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
                log.debug("Matched!");
                potentialLinks.add(newUrl);
            }
        } catch (MalformedURLException e) {
            log.warn("Bad URL " + e);
        }
    }
}