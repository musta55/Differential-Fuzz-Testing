/**
 * @param xpathExpressionContext Node
 * @param ns Namespaces declaration
 */
public PropertiesBasedPrefixResolverForXpath2(Node xpathExpressionContext, String ns) {
    super(xpathExpressionContext);
    this.namespaceMap = parseNamespaces(ns);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Parses the namespaces from the provided string and returns a map.
 *
 * @param ns Namespaces declaration
 * @return Map of prefixes to namespaces
 */
private static Map<String, String> parseNamespaces(String ns) {
    Map<String, String> namespaceMap = new HashMap<>();
    String namespace = ns.trim();
    if (!namespace.isEmpty()) {
        for (String prefixValue : namespace.split("\\s+")) {
            String[] keyAndValue = prefixValue.trim().split("=");
            if (keyAndValue.length == 2) {
                namespaceMap.put(keyAndValue[0], keyAndValue[1]);
            }
        }
    }
    return namespaceMap;
}

