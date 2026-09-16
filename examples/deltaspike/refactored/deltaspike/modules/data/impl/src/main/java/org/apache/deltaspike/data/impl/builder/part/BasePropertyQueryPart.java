package org.apache.deltaspike.data.impl.builder.part;

import org.apache.deltaspike.data.impl.builder.MethodExpressionException;
import org.apache.deltaspike.data.impl.meta.RepositoryMetadata;
import org.apache.deltaspike.data.impl.property.Property;
import org.apache.deltaspike.data.impl.property.query.NamedPropertyCriteria;
import org.apache.deltaspike.data.impl.property.query.PropertyQueries;
import org.apache.deltaspike.data.impl.property.query.PropertyQuery;

abstract class BasePropertyQueryPart extends QueryPart
{
    static final String SEPARATOR = "_";

    void validate(String name, String method, RepositoryMetadata repo)
    {
        Class<?> current = repo.getEntityMetadata().getEntityClass();
        if (name == null)
        {
            throw new MethodExpressionException(null, repo.getRepositoryClass(), method);
        }
        for (String property : splitName(name))
        {
            PropertyQuery<?> query = PropertyQueries.createQuery(current)
                    .addCriteria(new NamedPropertyCriteria(property));
            Property<?> result = query.getFirstResult();
            if (result == null)
            {
                throw new MethodExpressionException(property, repo.getRepositoryClass(), method);
            }
            current = result.getJavaClass();
        }
    }

    private String[] splitName(String name)
    {
        return name.split(SEPARATOR);
    }

    String rewriteSeparator(String name)
    {
        if (name.contains("_"))
        {
            return name.replaceAll(SEPARATOR, ".");
        }
        return name;
    }

}