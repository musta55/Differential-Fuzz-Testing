/**
 * Override TestElementConverter; convert HTTPSamplerBase to merge
 * the two means of providing file names into a single list.
 *
 * {@inheritDoc}
 */
@Override
public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    final HTTPSamplerBase httpSampler = (HTTPSamplerBase) super.unmarshal(reader, context);
    // Help convert existing JMX files which use HTTPSampler[2] nodes
    String nodeName = reader.getNodeName();
    if (nodeName.equals(HTTPSamplerFactory.HTTP_SAMPLER_JAVA)) {
        httpSampler.setImplementation(HTTPSamplerFactory.IMPL_JAVA);
    }
    if (nodeName.equals(HTTPSamplerFactory.HTTP_SAMPLER_APACHE)) {
        httpSampler.setImplementation(HTTPSamplerFactory.IMPL_HTTP_CLIENT4);
    }
    httpSampler.mergeFileProperties();
    return httpSampler;
}