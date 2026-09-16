@Override
protected QueryPart buildQuery(QueryBuilderContext ctx) {
    if (!first) {
        ctx.append(" and ");
    }
    buildQueryForChildren(ctx);
    return this;
}