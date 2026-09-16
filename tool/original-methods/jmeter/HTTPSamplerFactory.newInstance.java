/**
 * Create a new instance of the required sampler type
 *
 * @param alias HTTP_SAMPLER or HTTP_SAMPLER_APACHE or IMPL_HTTP_CLIENT3_1 or IMPL_HTTP_CLIENT4
 * @return the appropriate sampler
 * @throws UnsupportedOperationException if alias is not recognised
 */
public static HTTPSamplerBase newInstance(String alias) {
    if (alias == null || alias.length() == 0) {
        return new HTTPSamplerProxy();
    }
    if (alias.equals(HTTP_SAMPLER_JAVA) || alias.equals(IMPL_JAVA)) {
        return new HTTPSamplerProxy(IMPL_JAVA);
    }
    if (alias.equals(IMPL_HTTP_CLIENT4) || alias.equals(HTTP_SAMPLER_APACHE) || alias.equals(IMPL_HTTP_CLIENT3_1)) {
        return new HTTPSamplerProxy(IMPL_HTTP_CLIENT4);
    }
    throw new IllegalArgumentException("Unknown sampler type: '" + alias + "'");
}