/**
 * @param xpathExpressionContext Node
 * @param ns Namespaces declaration
 */
public PropertiesBasedPrefixResolverForXpath2(Node xpathExpressionContext, String ns) {
    super(xpathExpressionContext);
    String namespace = ns.trim();
    if (!namespace.isEmpty()) {
        for (String prefixValue : namespace.split("\\s+")) {
            String[] keyandvalue = prefixValue.trim().split("=");
            namespaceMap.put(keyandvalue[0], keyandvalue[1]);
        }
    }
}