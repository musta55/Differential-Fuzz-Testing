@Override
protected QueryPart buildQuery(QueryBuilderContext ctx) {
    appendAndIfNotFirst(ctx);
    buildQueryForChildren(ctx);
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void appendAndIfNotFirst(QueryBuilderContext ctx) {
    if (!first) {
        ctx.append(" and ");
    }
}

