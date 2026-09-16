package org.apache.deltaspike.data.impl.property.query;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A criteria that matches a property based on name
 * @see PropertyCriteria
 */
public class NamedPropertyCriteria implements PropertyCriteria
{
    private final String[] propertyNames;

    public NamedPropertyCriteria(String... propertyNames)
    {
        this.propertyNames = propertyNames;
    }

    @Override
    public boolean fieldMatches(Field f)
    {
        for (String propertyName : propertyNames)
        {
            if (propertyName.equals(f.getName()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean methodMatches(Method m)
    {
        for (String propertyName : propertyNames)
        {
            if (isGetterMethod(m, propertyName))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isGetterMethod(Method m, String propertyName)
    {
        return m.getName().startsWith("get") &&
               Introspector.decapitalize(m.getName().substring(3)).equals(propertyName);
    }
}