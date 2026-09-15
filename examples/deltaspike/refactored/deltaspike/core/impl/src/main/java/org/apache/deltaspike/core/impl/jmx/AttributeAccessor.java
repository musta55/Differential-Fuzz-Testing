package org.apache.deltaspike.core.impl.jmx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Just a helper class mapping a JMX attribute.
 * It make the link between the attribute and its accessors validating
 * operations (read/write) are possible.
 */
public class AttributeAccessor
{
    private final Method getter;
    private final Method setter;
    private final boolean presentAsTabularIfPossible;

    public AttributeAccessor(final Method get, final Method set, final boolean presentAsTabularIfPossible)
    {
        this.getter = get;
        this.setter = set;
        this.presentAsTabularIfPossible = presentAsTabularIfPossible;
    }

    public boolean isPresentAsTabularIfPossible()
    {
        return presentAsTabularIfPossible;
    }

    public Object get(final Object instance) throws InvocationTargetException, IllegalAccessException
    {
        if (getter == null)
        {
            throw new IllegalAccessException("This attribute has no getter");
        }
        return getter.invoke(instance);
    }

    public void set(final Object instance, final Object value) throws InvocationTargetException, IllegalAccessException
    {
        if (setter == null)
        {
            throw new IllegalAccessException("This attribute has no setter");
        }
        setter.invoke(instance, value);
    }
}