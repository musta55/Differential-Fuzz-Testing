package org.apache.deltaspike.data.impl.builder.part;

import static org.apache.deltaspike.data.impl.util.QueryUtils.splitByKeyword;

import org.apache.deltaspike.data.impl.builder.QueryBuilderContext;
import org.apache.deltaspike.data.impl.meta.RepositoryMetadata;

class OrQueryPart extends ConnectingQueryPart
{

    public OrQueryPart(boolean first)
    {
        super(first);
    }

    @Override
    protected QueryPart build(String queryPart, String method, RepositoryMetadata repo)
    {
        String[] andParts = splitByKeyword(queryPart, "And");
        processAndParts(andParts, method, repo);
        return this;
    }

    private void processAndParts(String[] andParts, String method, RepositoryMetadata repo)
    {
        boolean first = true;
        for (String and : andParts)
        {
            AndQueryPart andPart = new AndQueryPart(first);
            first = false;
            children.add(andPart.build(and, method, repo));
        }
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx)
    {
        if (!first)
        {
            ctx.append(" or ");
        }
        buildQueryForChildren(ctx);
        return this;
    }

}