public MetricsMetaInfo getMeta() {
    // Only read the id from the implementation when needed, to avoid uninitialized cases.
    this.metadata.setId(this.getEntityId());
    return metadata;
}