public MetricsMetaInfo getMeta() {
    // Only read the id from the implementation when needed, to avoid uninitialized cases.
    metadata.setId(getEntityId());
    return metadata;
}