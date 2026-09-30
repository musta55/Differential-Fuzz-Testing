public static <T> void register(final Class<? extends StringCodec<?>> codec, final Class<T> clazz) throws InstantiationException, IllegalAccessException {
    check();
    final StringCodec<?> codecInstance = codec.newInstance();
    ConvertUtils.register(getCodecConverter(codecInstance), clazz);
    codecs.put(clazz, codec);
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

