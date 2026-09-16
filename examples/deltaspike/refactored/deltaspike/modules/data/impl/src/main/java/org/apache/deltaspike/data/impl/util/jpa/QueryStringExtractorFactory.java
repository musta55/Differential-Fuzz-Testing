package org.apache.deltaspike.data.impl.util.jpa;

import javax.persistence.Query;

public class QueryStringExtractorFactory
{

    private final QueryStringExtractor[] extractors = new QueryStringExtractor[]
    {
        new HibernateQueryStringExtractor(),
        new EclipseLinkEjbQueryStringExtractor(),
        new OpenJpaQueryStringExtractor()
    };

    public String extract(final Query query)
    {
        for (final QueryStringExtractor extractor : extractors)
        {
            final String compare = extractor.getClass().getAnnotation(ProviderSpecific.class).value();
            final Object implQuery = getImplQuery(compare, query);
            if (implQuery != null)
            {
                return extractor.extractFrom(implQuery);
            }
        }
        throw new RuntimeException("Persistence provider not supported");
    }

    private Object getImplQuery(final String clazzName, final Query query)
    {
        try
        {
            Class<?> toClass = Class.forName(clazzName);
            try
            {
                // throw a persistence exception if not possible
                return query.unwrap(toClass);
            }
            catch (Exception e)
            {
                toClass.cast(query);
                return query;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

}