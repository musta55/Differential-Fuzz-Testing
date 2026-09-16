package org.apache.deltaspike.data.impl.param;

public class ToUpperStringParameterUpdate implements ParameterUpdate
{

    private final String id;

    public ToUpperStringParameterUpdate(String id)
    {
        this.id = id;
    }

    @Override
    public String forParamWithId()
    {
        return id;
    }

    @Override
    public Object newParamValue(Object current)
    {
        if (isString(current))
        {
            return toUpperCase((String) current);
        }
        return current;
    }

    private boolean isString(Object obj)
    {
        return obj instanceof String;
    }

    private String toUpperCase(String str)
    {
        return str.toUpperCase();
    }
}