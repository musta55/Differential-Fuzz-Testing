/**
 * @return the templates names sorted in alphabetical order
 */
public String[] getTemplateNames() {
    return allTemplates.keySet().toArray(new String[0]);
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

