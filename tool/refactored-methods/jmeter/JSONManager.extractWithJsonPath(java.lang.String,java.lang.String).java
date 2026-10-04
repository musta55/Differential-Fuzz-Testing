/**
 * @param jsonString JSON String from which data is extracted
 * @param jsonPath JSON-PATH expression
 * @return List of JSON Strings of the extracted data
 * @throws ParseException when parsing fails
 */
public List<Object> extractWithJsonPath(String jsonString, String jsonPath) throws ParseException {
    JsonPath jsonPathParser = getJsonPath(expressionToJsonPath, jsonPath);
    List<Object> extractedObjects;
    try {
        extractedObjects = jsonPathParser.read(jsonString, DEFAULT_CONFIGURATION);
    } catch (PathNotFoundException e) {
        if (log.isDebugEnabled()) {
            log.debug("Could not find JSON Path {} in [{}]: {}", jsonPath, jsonString, e.getLocalizedMessage());
        }
        return Collections.emptyList();
    }
    List<Object> results = new ArrayList<>(extractedObjects.size());
    for (Object obj : extractedObjects) {
        results.add(stringifyJSONObject(obj));
    }
    return Collections.unmodifiableList(results);
}
// ---- helper method(s) introduced by the refactoring ----
private static JsonPath getJsonPath(Map<String, JsonPath> cache, String jsonPathExpression) {
    return cache.computeIfAbsent(jsonPathExpression, JsonPath::compile);
}

