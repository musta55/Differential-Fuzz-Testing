/**
 * @param templates Map of {@link Template} referenced by name
 * @param templateNode {@link Node} the xml template node
 */
static void parseTemplateNode(Map<? super String, ? super Template> templates, Node templateNode) {
    if (templateNode.getNodeType() == Node.ELEMENT_NODE) {
        Template template = new Template();
        Element element = (Element) templateNode;
        template.setTestPlan("true".equals(element.getAttribute("isTestPlan")));
        template.setName(textOfFirstTag(element, "name"));
        template.setDescription(textOfFirstTag(element, "description"));
        template.setFileName(textOfFirstTag(element, "fileName"));
        NodeList nl = element.getElementsByTagName("parameters");
        if (nl.getLength() > 0) {
            NodeList parameterNodes = ((Element) nl.item(0)).getElementsByTagName("parameter");
            Map<String, String> parameters = parseParameterNodes(parameterNodes);
            template.setParameters(parameters);
        }
        templates.put(template.getName(), template);
    }
}