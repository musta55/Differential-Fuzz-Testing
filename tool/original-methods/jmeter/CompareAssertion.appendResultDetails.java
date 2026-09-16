private static void appendResultDetails(StringBuilder buf, SampleResult result) {
    final String samplerData = result.getSamplerData();
    if (samplerData != null) {
        buf.append(samplerData.trim());
    }
    //$NON-NLS-1$
    buf.append("\n");
    final String requestHeaders = result.getRequestHeaders();
    if (requestHeaders != null) {
        buf.append(requestHeaders);
    }
    //$NON-NLS-1$
    buf.append("\n\n");
}