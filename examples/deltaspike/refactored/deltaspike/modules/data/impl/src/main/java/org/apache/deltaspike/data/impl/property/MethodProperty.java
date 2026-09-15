package org.apache.deltaspike.data.impl.property;

import java.lang.reflect.Method;

public interface MethodProperty<V> extends Property<V>
{
    @Override
    Method getAnnotatedElement();
}