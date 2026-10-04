/**
 * {@inheritDoc}
 */
@Override
public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    ScriptWrapper wrap = new ScriptWrapper();
    wrap.version = reader.getAttribute(ATT_VERSION);
    // Make sure decoding
    ConversionHelp.setInVersion(wrap.version);
    // follows input file
    reader.moveDown();
    // Catch errors and rethrow as ConversionException so we get location details
    try {
        wrap.testPlan = (HashTree) context.convertAnother(wrap, getNextType(reader));
    } catch (NoClassDefFoundError | Exception e) {
        throw createConversionException(e);
    }
    return wrap;
}