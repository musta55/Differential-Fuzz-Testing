/**
 * @see org.apache.jmeter.extractor.Extractor#extract(String, String, int, String, List, int, String)
 */
@Override
public int extract(String expression, String attribute, int matchNumber, String inputString, List<String> result, int found, String cacheKey) {
    NodeSelector nodeSelector;
    if (cacheKey != null) {
        nodeSelector = (NodeSelector) JMeterContextService.getContext().getSamplerContext().get(CACHE_KEY_PREFIX + cacheKey);
        if (nodeSelector == null) {
            LagartoDOMBuilder domBuilder = new LagartoDOMBuilder();
            jodd.lagarto.dom.Document doc = domBuilder.parse(inputString);
            nodeSelector = new NodeSelector(doc);
            JMeterContextService.getContext().getSamplerContext().put(CACHE_KEY_PREFIX + cacheKey, nodeSelector);
        }
    } else {
        LagartoDOMBuilder domBuilder = new LagartoDOMBuilder();
        jodd.lagarto.dom.Document doc = domBuilder.parse(inputString);
        nodeSelector = new NodeSelector(doc);
    }
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