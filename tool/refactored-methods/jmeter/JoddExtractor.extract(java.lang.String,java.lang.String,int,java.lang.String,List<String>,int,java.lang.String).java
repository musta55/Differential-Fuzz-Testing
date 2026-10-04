/**
 * @see org.apache.jmeter.extractor.Extractor#extract(String, String, int, String, List, int, String)
 */
@Override
public int extract(String expression, String attribute, int matchNumber, String inputString, List<String> result, int found, String cacheKey) {
    NodeSelector nodeSelector = getNodeSelector(inputString, cacheKey);
    List<List<CssSelector>> cssSelectors = CSS_SELECTOR_CACHE.get(expression);
    List<Node> elements = nodeSelector.select(cssSelectors);
    for (Node element : elements) {
        if (matchNumber <= 0 || found != matchNumber) {
            result.add(extractValue(attribute, element));
            found++;
        } else {
            break;
        }
    }
    return found;
}
// ---- helper method(s) introduced by the refactoring ----
private static NodeSelector getNodeSelector(String inputString, String cacheKey) {
    if (cacheKey != null) {
        NodeSelector nodeSelector = (NodeSelector) JMeterContextService.getContext().getSamplerContext().get(CACHE_KEY_PREFIX + cacheKey);
        if (nodeSelector == null) {
            nodeSelector = createNodeSelector(inputString);
            JMeterContextService.getContext().getSamplerContext().put(CACHE_KEY_PREFIX + cacheKey, nodeSelector);
        }
        return nodeSelector;
    } else {
        return createNodeSelector(inputString);
    }
}

private static NodeSelector createNodeSelector(String inputString) {
    LagartoDOMBuilder domBuilder = new LagartoDOMBuilder();
    jodd.lagarto.dom.Document doc = domBuilder.parse(inputString);
    return new NodeSelector(doc);
}

