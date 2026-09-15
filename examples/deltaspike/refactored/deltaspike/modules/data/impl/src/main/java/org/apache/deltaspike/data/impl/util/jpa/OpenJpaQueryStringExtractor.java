package org.apache.deltaspike.data.impl.util.jpa;

@ProviderSpecific("org.apache.openjpa.persistence.OpenJPAQuery")
public class OpenJpaQueryStringExtractor extends BaseQueryStringExtractor
{
    @Override
    public String extractFrom(Object query)
    {
        return (String) invoke("getQueryString", query);
    }
}