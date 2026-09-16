package org.apache.deltaspike.data.impl.property;

import java.lang.reflect.Field;

public interface FieldProperty<V> extends Property<V>
{
    @Override
    Field getAnnotatedElement();
}