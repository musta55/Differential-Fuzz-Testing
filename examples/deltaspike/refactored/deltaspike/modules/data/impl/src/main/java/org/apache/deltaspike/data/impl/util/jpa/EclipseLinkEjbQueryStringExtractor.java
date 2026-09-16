package org.apache.deltaspike.data.impl.util.jpa;

@ProviderSpecific("org.eclipse.persistence.jpa.JpaQuery")
public class EclipseLinkEjbQueryStringExtractor extends BaseQueryStringExtractor
{
    @Override
    public String extractFrom(Object query)
    {
        Object dbQuery = invoke("getDatabaseQuery", query);
        return (String) invoke("getJPQLString", dbQuery);
    }
}