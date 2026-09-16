/**
 * Create a new instance of the required sampler type
 *
 * @param alias HTTP_SAMPLER or HTTP_SAMPLER_APACHE or IMPL_HTTP_CLIENT3_1 or IMPL_HTTP_CLIENT4
 * @return the appropriate sampler
 * @throws UnsupportedOperationException if alias is not recognised
 */
public static HTTPSamplerBase newInstance(String alias) {
    if (JOrphanUtils.isBlank(alias)) {
        return new HTTPSamplerProxy();
    }
    Map<String, String> aliasToImplMap = new HashMap<>();
    aliasToImplMap.put(HTTP_SAMPLER_JAVA, IMPL_JAVA);
    aliasToImplMap.put(IMPL_JAVA, IMPL_JAVA);
    aliasToImplMap.put(IMPL_HTTP_CLIENT4, IMPL_HTTP_CLIENT4);
    aliasToImplMap.put(HTTP_SAMPLER_APACHE, IMPL_HTTP_CLIENT4);
    aliasToImplMap.put(IMPL_HTTP_CLIENT3_1, IMPL_HTTP_CLIENT4);
    String impl = aliasToImplMap.getOrDefault(alias, null);
    if (impl != null) {
        return new HTTPSamplerProxy(impl);
    }
    throw new IllegalArgumentException("Unknown sampler type: '" + alias + "'");
}