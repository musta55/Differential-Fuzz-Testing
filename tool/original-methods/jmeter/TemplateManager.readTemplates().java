private Map<String, Template> readTemplates() {
    final Map<String, Template> temps = new TreeMap<>();
    final String[] templateFiles = TEMPLATE_FILES.split(",");
    for (String templateFile : templateFiles) {
        if (!StringUtils.isEmpty(templateFile)) {
            final File file = new File(JMeterUtils.getJMeterHome(), templateFile);
            try {
                if (file.exists() && file.canRead()) {
                    if (log.isInfoEnabled()) {
                        log.info("Reading templates from: {}", file.getAbsolutePath());
                    }
                    Map<String, Template> templates = parseTemplateFile(file);
                    final File parent = file.getParentFile();
                    for (Template t : templates.values()) {
                        if (!t.getFileName().startsWith("/")) {
                            t.setParent(parent);
                        }
                    }
                    temps.putAll(templates);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Ignoring template file:'{}' as it does not exist or is not readable", file.getAbsolutePath());
                    }
                }
            } catch (Exception ex) {
                if (log.isWarnEnabled()) {
                    log.warn("Ignoring template file:'{}', an error occurred parsing the file", file.getAbsolutePath(), ex);
                }
            }
        }
    }
    return temps;
}