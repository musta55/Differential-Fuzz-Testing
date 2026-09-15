package org.apache.deltaspike.data.impl.param;

import javax.persistence.Query;

/**
 * Query parameters which have an index (?1).
 */
public class IndexedParameter extends Parameter
{

    private final int index;

    public IndexedParameter(int index, Object value)
    {
        super(value);
        this.index = index;
    }

    @Override
    public void apply(Query query)
    {
        query.setParameter(index, queryValue());
    }

    @Override
    public boolean is(String identifier)
    {
        try
        {
            return Integer.valueOf(identifier).intValue() == index;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

}