public static void loadDefaultConverters() {
    LOG.debug("Loading default converters for BeanUtils");
    ConvertUtils.register(getStringConverter(), String.class);
    ConvertUtils.register(getUriConverter(), URI.class);
}
// ---- helper method(s) introduced by the refactoring ----
private static Converter getStringConverter() {
    return new Converter() {

        @Override
        @SuppressWarnings("unchecked")
        public Object convert(Class type, Object value) {
            if (value == null) {
                return null;
            }
            for (Class<?> clazz = value.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                Class<? extends StringCodec> codec = codecs.get(clazz);
                if (codec == null) {
                    continue;
                }
                StringCodec instance;
                try {
                    instance = codec.newInstance();
                } catch (IllegalAccessException | InstantiationException ex) {
                    throw new RuntimeException("Internal Error - it's impossible for this exception to be thrown!", ex);
                }
                return instance.toString(value);
            }
            return value.toString();
        }
    };
}

private static Converter getUriConverter() {
    return new Converter() {

        @Override
        public Object convert(Class type, Object value) {
            return value == null ? null : URI.create(value.toString());
        }
    };
}

private static Converter getCodecConverter(final StringCodec<?> codecInstance) {
    return new Converter() {

        @Override
        public Object convert(Class type, Object value) {
            return value == null ? null : codecInstance.fromString(value.toString());
        }
    };
}

