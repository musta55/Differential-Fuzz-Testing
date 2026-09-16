public Set<Annotation> getQualifiers() {
    if (qualifiers == null) {
        return Collections.emptySet();
    }
    return Collections.unmodifiableSet(this.qualifiers);
}