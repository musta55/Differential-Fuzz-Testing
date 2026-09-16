void validate(String name, String method, RepositoryMetadata repo) {
    Class<?> current = repo.getEntityMetadata().getEntityClass();
    if (name == null) {
        throw new MethodExpressionException(null, repo.getRepositoryClass(), method);
    }
    for (String property : name.split(SEPARATOR)) {
        PropertyQuery<?> query = PropertyQueries.createQuery(current).addCriteria(new NamedPropertyCriteria(property));
        Property<?> result = query.getFirstResult();
        if (result == null) {
            throw new MethodExpressionException(property, repo.getRepositoryClass(), method);
        }
        current = result.getJavaClass();
    }
}