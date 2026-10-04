protected Class<?> getNextType(HierarchicalStreamReader reader) {
    String classAttribute = reader.getAttribute(ConversionHelp.ATT_CLASS);
    Class<?> type;
    if (classAttribute == null) {
        type = classMapper.realClass(reader.getNodeName());
    } else {
        type = classMapper.realClass(classAttribute);
    }
    return type;
}