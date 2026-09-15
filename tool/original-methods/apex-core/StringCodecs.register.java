public static <T> void register(final Class<? extends StringCodec<?>> codec, final Class<T> clazz) throws InstantiationException, IllegalAccessException {
    check();
    final StringCodec<?> codecInstance = codec.newInstance();
    ConvertUtils.register(new Converter() {

        @Override
        public Object convert(Class type, Object value) {
            return value == null ? null : codecInstance.fromString(value.toString());
        }
    }, clazz);
    codecs.put(clazz, codec);
}