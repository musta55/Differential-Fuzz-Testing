public AccessLogSamplerBeanInfo() {
    super(AccessLogSampler.class);
    log.debug("Entered access log sampler bean info");
    // $NON-NLS-1$
    createPropertyGroup(// $NON-NLS-1$
    "defaults", // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$ $NON-NLS-4$
    new String[] { "protocol", "domain", "portString", "imageParsing" });
    // $NON-NLS-1$
    createPropertyGroup(// $NON-NLS-1$
    "plugins", // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
    new String[] { "parserClassName", "filterClassName" });
    // $NON-NLS-1$
    createPropertyGroup(// $NON-NLS-1$
    "accesslogfile", // $NON-NLS-1$
    new String[] { "logFile" });
    PropertyDescriptor p;
    p = property("parserClassName");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, AccessLogSampler.DEFAULT_CLASS);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    log.debug("found parsers: {}", LOG_PARSER_CLASSES);
    p.setValue(TAGS, LOG_PARSER_CLASSES.toArray(new String[0]));
    // $NON-NLS-1$
    p = property("filterClassName");
    p.setValue(NOT_UNDEFINED, Boolean.FALSE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    String[] filterClasses = loadServiceClasses(Filter.class).toArray(new String[0]);
    p.setValue(TAGS, filterClasses);
    // $NON-NLS-1$
    p = property("logFile");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "");
    p.setPropertyEditorClass(FileEditor.class);
    // $NON-NLS-1$
    p = property("domain");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, "");
    // $NON-NLS-1$
    p = property("protocol");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "http");
    p.setValue(DEFAULT_NOT_SAVED, Boolean.TRUE);
    // $NON-NLS-1$
    p = property("portString");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    // $NON-NLS-1$
    p.setValue(DEFAULT, "");
    // $NON-NLS-1$
    p = property("imageParsing");
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
    p.setValue(NOT_OTHER, Boolean.TRUE);
    log.debug("Got to end of access log sampler bean info init");
}
// ---- helper method(s) introduced by the refactoring ----
private static <T> List<String> loadServiceClasses(Class<T> serviceClass) {
    return JMeterUtils.loadServicesAndScanJars(serviceClass, ServiceLoader.load(serviceClass), Thread.currentThread().getContextClassLoader(), new LogAndIgnoreServiceLoadExceptionHandler(log)).stream().map(s -> s.getClass().getName()).sorted().collect(Collectors.toList());
}

