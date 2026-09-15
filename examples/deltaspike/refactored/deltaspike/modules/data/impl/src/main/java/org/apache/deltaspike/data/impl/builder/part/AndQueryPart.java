package org.apache.deltaspike.data.impl.builder.part;

import org.apache.deltaspike.data.impl.builder.QueryBuilderContext;
import org.apache.deltaspike.data.impl.meta.RepositoryMetadata;

class AndQueryPart extends ConnectingQueryPart
{

    public AndQueryPart(boolean first)
    {
        super(first);
    }

    @Override
    protected QueryPart build(String queryPart, String method, RepositoryMetadata repo)
    {
        children.add(new PropertyQueryPart().build(queryPart, method, repo));
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx)
    {
        appendAndIfNotFirst(ctx);
        buildQueryForChildren(ctx);
        return this;
    }

    private void appendAndIfNotFirst(QueryBuilderContext ctx)
    {
        if (!first)
        {
            ctx.append(" and ");
        }
    }

}