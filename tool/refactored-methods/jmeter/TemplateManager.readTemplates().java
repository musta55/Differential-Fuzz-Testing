private Map<String, Template> readTemplates() {
    final Map<String, Template> temps = new TreeMap<>();
    final String[] templateFiles = TEMPLATE_FILES.split(",");
    for (String templateFile : templateFiles) {
        if (StringUtils.isNotEmpty(templateFile)) {
            final File file = new File(JMeterUtils.getJMeterHome(), templateFile);
            try {
                if (file.exists() && file.canRead()) {
                    logIfInfoEnabled("Reading templates from: {}", file.getAbsolutePath());
                    Map<String, Template> templates = parseTemplateFile(file);
                    final File parent = file.getParentFile();
                    for (Template t : templates.values()) {
                        if (!t.getFileName().startsWith("/")) {
                            t.setParent(parent);
                        }
                    }
                    temps.putAll(templates);
                } else {
                    logIfWarnEnabled("Ignoring template file:'{}' as it does not exist or is not readable", file.getAbsolutePath());
                }
            } catch (Exception ex) {
                logIfWarnEnabled("Ignoring template file:'{}', an error occurred parsing the file", file.getAbsolutePath(), ex);
            }
        }
    }
    return temps;
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

