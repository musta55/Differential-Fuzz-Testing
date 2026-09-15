public static void check() {
    if (classLoaders.putIfAbsent(Thread.currentThread().getContextClassLoader(), Boolean.TRUE) == null) {
        loadDefaultConverters();
        for (Map.Entry<Class<?>, Class<? extends StringCodec<?>>> entry : codecs.entrySet()) {
            try {
                final StringCodec<?> codecInstance = entry.getValue().newInstance();
                ConvertUtils.register(getCodecConverter(codecInstance), entry.getKey());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
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

