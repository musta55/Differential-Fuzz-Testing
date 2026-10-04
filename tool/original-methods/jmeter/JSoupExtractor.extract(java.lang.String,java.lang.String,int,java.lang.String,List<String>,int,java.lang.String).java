/**
 * @see Extractor#extract(String, String, int, String, List, int, String)
 */
@Override
public int extract(String expression, String attribute, int matchNumber, String inputString, List<String> result, int found, String cacheKey) {
    Document document;
    if (cacheKey != null) {
        document = (Document) JMeterContextService.getContext().getSamplerContext().get(CACHE_KEY_PREFIX + cacheKey);
        if (document == null) {
            document = Jsoup.parse(inputString);
            JMeterContextService.getContext().getSamplerContext().put(CACHE_KEY_PREFIX + cacheKey, document);
        }
    } else {
        document = Jsoup.parse(inputString);
    }
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