@Override
protected QueryPart build(String queryPart, String method, RepositoryMetadata repo) {
    String[] andParts = splitByKeyword(queryPart, "And");
    boolean first = true;
    for (String and : andParts) {
        AndQueryPart andPart = new AndQueryPart(first);
        first = false;
        children.add(andPart.build(and, method, repo));
    }
    return this;
}