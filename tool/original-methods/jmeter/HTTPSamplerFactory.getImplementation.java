public static HTTPAbstractImpl getImplementation(String impl, HTTPSamplerBase base) {
    if (HTTPSamplerBase.PROTOCOL_FILE.equals(base.getProtocol())) {
        return new HTTPFileImpl(base);
    }
    if (JOrphanUtils.isBlank(impl)) {
        impl = DEFAULT_CLASSNAME;
    }
    if (IMPL_JAVA.equals(impl) || HTTP_SAMPLER_JAVA.equals(impl)) {
        return new HTTPJavaImpl(base);
    } else if (IMPL_HTTP_CLIENT4.equals(impl) || IMPL_HTTP_CLIENT3_1.equals(impl)) {
        return new HTTPHC4Impl(base);
    } else {
        throw new IllegalArgumentException("Unknown implementation type: '" + impl + "'");
    }
}