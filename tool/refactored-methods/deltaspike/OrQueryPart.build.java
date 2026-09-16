@Override
protected QueryPart build(String queryPart, String method, RepositoryMetadata repo) {
    String[] andParts = splitByKeyword(queryPart, "And");
    processAndParts(andParts, method, repo);
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void processAndParts(String[] andParts, String method, RepositoryMetadata repo) {
    boolean first = true;
    for (String and : andParts) {
        AndQueryPart andPart = new AndQueryPart(first);
        first = false;
        children.add(andPart.build(and, method, repo));
    }
}

