/**
 * {@inheritDoc}
 */
@Override
public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    ScriptWrapper wrap = new ScriptWrapper();
    wrap.version = reader.getAttribute(ATT_VERSION);
    // Make sure decoding follows input file
    ConversionHelp.setInVersion(wrap.version);
    reader.moveDown();
    // Catch errors and rethrow as ConversionException so we get location details
    try {
        wrap.testPlan = (HashTree) context.convertAnother(wrap, getNextType(reader));
    } catch (NoClassDefFoundError | Exception e) {
        throw createConversionExceptionWithStackTrace(e);
    }
    return wrap;
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

