package org.apache.deltaspike.data.impl.builder.postprocessor;

import static org.apache.deltaspike.core.util.StringUtils.isNotEmpty;
import static org.apache.deltaspike.data.impl.util.QueryUtils.nullSafeValue;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.apache.deltaspike.data.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.data.impl.handler.JpaQueryPostProcessor;
import org.apache.deltaspike.data.impl.param.Parameters;
import org.apache.deltaspike.data.impl.util.jpa.QueryStringExtractorFactory;

public class CountQueryPostProcessor implements JpaQueryPostProcessor
{

    private static final Logger log = Logger.getLogger(CountQueryPostProcessor.class.getName());

    private final QueryStringExtractorFactory factory = new QueryStringExtractorFactory();

    @Override
    public Query postProcess(CdiQueryInvocationContext context, Query query)
    {
        String queryString = getQueryString(context, query);
        QueryExtraction extract = new QueryExtraction(queryString);
        String count = extract.rewriteToCount();
        log.log(Level.FINER, "Rewrote query {0} to {1}", new Object[] { queryString, count });
        Query result = context.getEntityManager().createQuery(count);
        Parameters params = context.getParams();
        params.applyTo(result);
        return result;
    }

    private String getQueryString(CdiQueryInvocationContext context, Query query)
    {
        if (isNotEmpty(context.getQueryString()))
        {
            return context.getQueryString();
        }
        return factory.extract(query);
    }

    private static class QueryExtraction
    {

        private String select;
        private String from;
        private String where;

        private String entityName;
        private final String query;

        public QueryExtraction(String query)
        {
            this.query = query;
        }

        public String rewriteToCount()
        {
            splitQuery();
            extractEntityName();
            return rewrite();
        }

        private String rewrite()
        {
            return new StringBuilder()
                    .append("select count(")
                        .append(nullSafeValue(select, entityName))
                    .append(") ")
                    .append(from)
                    .append(nullSafeValue(where))
                    .toString();
        }

        private void extractEntityName()
        {
            String[] split = from.split(" ");
            entityName = split.length > 1 ? split[split.length - 1] : "*";
        }

        private void splitQuery()
        {
            String lower = query.toLowerCase();
            int selectIndex = lower.indexOf("select");
            int fromIndex = lower.indexOf("from");
            int whereIndex = lower.indexOf("where");
            int orderByIndex = lower.indexOf("order by");

            select = extractSelect(lower, selectIndex, fromIndex);
            from = extractFrom(lower, fromIndex, whereIndex, orderByIndex);
            where = extractWhere(lower, whereIndex, orderByIndex);
        }

        private String extractSelect(String lower, int selectIndex, int fromIndex)
        {
            return selectIndex >= 0 ? query.substring(selectIndex + "select".length(), fromIndex).trim() : "";
        }

        private String extractFrom(String lower, int fromIndex, int whereIndex, int orderByIndex)
        {
            if (whereIndex >= 0)
            {
                return query.substring(fromIndex, whereIndex).trim();
            }
            else if (orderByIndex >= 0)
            {
                return query.substring(fromIndex, orderByIndex).trim();
            }
            return query.substring(fromIndex).trim();
        }

        private String extractWhere(String lower, int whereIndex, int orderByIndex)
        {
            if (whereIndex >= 0)
            {
                String whereClause = query.substring(whereIndex);
                if (orderByIndex > 0)
                {
                    whereClause = whereClause.substring(0, orderByIndex - whereIndex).trim();
                }
                return whereClause;
            }
            return "";
        }
    }
}