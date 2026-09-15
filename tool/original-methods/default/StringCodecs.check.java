public static void check() {
    if (classLoaders.putIfAbsent(Thread.currentThread().getContextClassLoader(), Boolean.TRUE) == null) {
        loadDefaultConverters();
        for (Map.Entry<Class<?>, Class<? extends StringCodec<?>>> entry : codecs.entrySet()) {
            try {
                final StringCodec<?> codecInstance = entry.getValue().newInstance();
                ConvertUtils.register(new Converter() {

                    @Override
                    public Object convert(Class type, Object value) {
                        return value == null ? null : codecInstance.fromString(value.toString());
                    }
                }, entry.getKey());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}