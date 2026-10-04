public static HTTPAbstractImpl getImplementation(String impl, HTTPSamplerBase base) {
    if (HTTPSamplerBase.PROTOCOL_FILE.equals(base.getProtocol())) {
        return new HTTPFileImpl(base);
    }
    if (JOrphanUtils.isBlank(impl)) {
        impl = DEFAULT_CLASSNAME;
    }
    Map<String, Class<? extends HTTPAbstractImpl>> implToClassMap = new HashMap<>();
    implToClassMap.put(IMPL_JAVA, HTTPJavaImpl.class);
    implToClassMap.put(HTTP_SAMPLER_JAVA, HTTPJavaImpl.class);
    implToClassMap.put(IMPL_HTTP_CLIENT4, HTTPHC4Impl.class);
    implToClassMap.put(IMPL_HTTP_CLIENT3_1, HTTPHC4Impl.class);
    Class<? extends HTTPAbstractImpl> clazz = implToClassMap.getOrDefault(impl, null);
    if (clazz != null) {
        try {
            return clazz.getDeclaredConstructor(HTTPSamplerBase.class).newInstance(base);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to instantiate implementation type: '" + impl + "'", e);
        }
    }
    throw new IllegalArgumentException("Unknown implementation type: '" + impl + "'");
}