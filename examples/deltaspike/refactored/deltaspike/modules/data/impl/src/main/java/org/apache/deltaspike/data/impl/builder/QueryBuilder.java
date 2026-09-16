package org.apache.deltaspike.data.impl.builder;

import org.apache.deltaspike.data.api.QueryResult;
import org.apache.deltaspike.data.api.mapping.QueryInOutMapper;
import org.apache.deltaspike.data.impl.handler.CdiQueryInvocationContext;

import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.List;

/**
 * Query builder factory. Delegates to concrete implementations.
 */
public abstract class QueryBuilder
{
    public static final String QUERY_SELECT = "select e from {0} e";
    public static final String QUERY_COUNT = "select count(e) from {0} e";
    public static final String QUERY_DELETE = "delete from {0} e";
    public static final String ENTITY_NAME = "e";

    public static String selectQuery(String entityName)
    {
        return MessageFormat.format(QUERY_SELECT, entityName);
    }

    public static String deleteQuery(String entityName)
    {
        return MessageFormat.format(QUERY_DELETE, entityName);
    }

    public static String countQuery(String entityName)
    {
        return MessageFormat.format(QUERY_COUNT, entityName);
    }

    @SuppressWarnings("unchecked")
    public Object executeQuery(CdiQueryInvocationContext context)
    {
        Object result = execute(context);
        if (shouldMapResult(result, context))
        {
            QueryInOutMapper<Object> mapper = (QueryInOutMapper<Object>) context.getQueryInOutMapper();
            return mapResult(mapper, result);
        }
        return result;
    }

    protected abstract Object execute(CdiQueryInvocationContext ctx);

    private boolean shouldMapResult(Object result, CdiQueryInvocationContext context)
    {
        return !isUnmappableResult(result) && context.hasQueryInOutMapper();
    }

    private Object mapResult(QueryInOutMapper<Object> mapper, Object result)
    {
        if (result instanceof List)
        {
            return mapper.mapResultList((List<Object>) result);
        }
        return mapper.mapResult(result);
    }

    private boolean isUnmappableResult(Object result)
    {
        return result instanceof QueryResult || result instanceof Query;
    }
}