void validate(String name, String method, RepositoryMetadata repo) {
    Class<?> current = repo.getEntityMetadata().getEntityClass();
    if (name == null) {
        throw new MethodExpressionException(null, repo.getRepositoryClass(), method);
    }
    for (String property : splitName(name)) {
        PropertyQuery<?> query = PropertyQueries.createQuery(current).addCriteria(new NamedPropertyCriteria(property));
        Property<?> result = query.getFirstResult();
        if (result == null) {
            throw new MethodExpressionException(property, repo.getRepositoryClass(), method);
        }
        current = result.getJavaClass();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private String[] splitName(String name) {
    return name.split(SEPARATOR);
}

