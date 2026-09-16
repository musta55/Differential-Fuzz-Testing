public String extract(final Query query) {
    for (final QueryStringExtractor extractor : extractors) {
        final String compare = extractor.getClass().getAnnotation(ProviderSpecific.class).value();
        final Object implQuery = toImplQuery(compare, query);
        if (implQuery != null) {
            return extractor.extractFrom(implQuery);
        }
    }
    throw new RuntimeException("Persistence provider not supported");
}