/**
 * @see Extractor#extract(String, String, int, String, List, int, String)
 */
@Override
public int extract(String expression, String attribute, int matchNumber, String inputString, List<String> result, int found, String cacheKey) {
    Document document = parseDocument(inputString, cacheKey);
    Elements elements = document.select(expression);
    for (Element element : elements) {
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
/**
 * Parses the document from the input string or cache.
 *
 * @param inputString the input string to parse
 * @param cacheKey the cache key to use for caching the parsed document
 * @return the parsed document
 */
private static Document parseDocument(String inputString, String cacheKey) {
    if (cacheKey != null) {
        Document document = (Document) JMeterContextService.getContext().getSamplerContext().get(CACHE_KEY_PREFIX + cacheKey);
        if (document == null) {
            document = Jsoup.parse(inputString);
            JMeterContextService.getContext().getSamplerContext().put(CACHE_KEY_PREFIX + cacheKey, document);
        }
        return document;
    } else {
        return Jsoup.parse(inputString);
    }
}

