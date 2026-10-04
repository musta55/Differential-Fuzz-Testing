/**
 * @param templates Map of {@link Template} referenced by name
 * @param templateNode {@link Node} the xml template node
 */
static void parseTemplateNode(Map<String, Template> templates, Node templateNode) {
    if (templateNode.getNodeType() == Node.ELEMENT_NODE) {
        Template template = new Template();
        Element element = (Element) templateNode;
        template.setTestPlan("true".equals(element.getAttribute("isTestPlan")));
        template.setName(getTextContent(element, "name"));
        template.setDescription(getTextContent(element, "description"));
        template.setFileName(getTextContent(element, "fileName"));
        NodeList nl = element.getElementsByTagName("parameters");
        if (nl.getLength() > 0) {
            NodeList parameterNodes = ((Element) nl.item(0)).getElementsByTagName("parameter");
            Map<String, String> parameters = parseParameterNodes(parameterNodes);
            template.setParameters(parameters);
        }
        templates.put(template.getName(), template);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void logIfInfoEnabled(String message, Object... args) {
    if (log.isInfoEnabled()) {
        log.info(message, args);
    }
}

private static void logIfWarnEnabled(String message, Object... args) {
    if (log.isWarnEnabled()) {
        log.warn(message, args);
    }
}

private static String getTextContent(Element element, String tagName) {
    NodeList nodeList = element.getElementsByTagName(tagName);
    return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : "";
}

