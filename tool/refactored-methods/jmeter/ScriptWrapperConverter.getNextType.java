protected Class<?> getNextType(HierarchicalStreamReader reader) {
    String classAttribute = reader.getAttribute(ConversionHelp.ATT_CLASS);
    return classAttribute == null ? classMapper.realClass(reader.getNodeName()) : classMapper.realClass(classAttribute);
}
// ---- helper method(s) introduced by the refactoring ----
private static ConversionException createConversionExceptionWithStackTrace(Throwable e) {
    ConversionException conversionException = new ConversionException(e);
    addFirstJMeterClassToException(conversionException, e.getStackTrace());
    return conversionException;
}

private static void addFirstJMeterClassToException(ConversionException conversionException, StackTraceElement[] stackTrace) {
    if (stackTrace != null) {
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("org.apache.jmeter.")) {
                conversionException.add("first-jmeter-class", element.toString());
                break;
            }
        }
    }
}

