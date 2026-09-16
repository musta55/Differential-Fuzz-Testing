/**
 * Override TestElementConverter; convert HTTPSamplerBase to merge
 * the two means of providing file names into a single list.
 *
 * {@inheritDoc}
 */
@Override
public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    final HTTPSamplerBase httpSampler = (HTTPSamplerBase) super.unmarshal(reader, context);
    setImplementationBasedOnNodeName(httpSampler, reader.getNodeName());
    httpSampler.mergeFileProperties();
    return httpSampler;
}
// ---- helper method(s) introduced by the refactoring ----
private static void setImplementationBasedOnNodeName(HTTPSamplerBase httpSampler, String nodeName) {
    if (nodeName.equals(HTTPSamplerFactory.HTTP_SAMPLER_JAVA)) {
        httpSampler.setImplementation(HTTPSamplerFactory.IMPL_JAVA);
    } else if (nodeName.equals(HTTPSamplerFactory.HTTP_SAMPLER_APACHE)) {
        httpSampler.setImplementation(HTTPSamplerFactory.IMPL_HTTP_CLIENT4);
    }
}

