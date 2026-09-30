public static void loadDefaultConverters() {
    LOG.debug("Loading default converters for BeanUtils");
    ConvertUtils.register(new Converter() {

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
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Internal Error - it's impossible for this exception to be thrown!", ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException("Internal Error - it's impossible for this exception to be thrown!", ex);
                }
                return instance.toString(value);
            }
            return value.toString();
        }
    }, String.class);
    ConvertUtils.register(new Converter() {

        @Override
        public Object convert(Class type, Object value) {
            return value == null ? null : URI.create(value.toString());
        }
    }, URI.class);
}