package org.apache.deltaspike.data.impl.param;

import javax.persistence.Query;

/**
 * Parameters which have a name (:name).
 */
public class NamedParameter extends Parameter
{
    private final String name;

    public NamedParameter(String name, Object value)
    {
        super(value);
        this.name = name;
    }

    @Override
    public void apply(Query query)
    {
        query.setParameter(name, queryValue());
    }

    @Override
    public boolean is(String ident)
    {
        return ident != null && ident.equals(name);
    }
}