package org.apache.deltaspike.core.util;

import javax.enterprise.inject.Typed;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * A basic implementation of {@link ParameterizedType}.
 */
@Typed()
class ParameterizedTypeImpl implements ParameterizedType
{
    private final Type[] actualTypeArguments;
    private final Type rawType;
    private final Type ownerType;

    ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType)
    {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    public Type[] getActualTypeArguments()
    {
        return Arrays.copyOf(actualTypeArguments, actualTypeArguments.length);
    }

    public Type getOwnerType()
    {
        return ownerType;
    }

    public Type getRawType()
    {
        return rawType;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(actualTypeArguments) ^ Objects.hashCode(ownerType) ^ Objects.hashCode(rawType);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof ParameterizedType))
        {
            return false;
        }
        ParameterizedType that = (ParameterizedType) obj;
        return ownerTypesEqual(that) && rawTypesEqual(that) && actualTypeArgumentsEqual(that);
    }

    private boolean ownerTypesEqual(ParameterizedType that)
    {
        return Objects.equals(ownerType, that.getOwnerType());
    }

    private boolean rawTypesEqual(ParameterizedType that)
    {
        return Objects.equals(rawType, that.getRawType());
    }

    private boolean actualTypeArgumentsEqual(ParameterizedType that)
    {
        return Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(rawType);
        if (actualTypeArguments.length > 0)
        {
            sb.append("<");
            sb.append(String.join(",", Arrays.stream(actualTypeArguments).map(Type::toString).toArray(String[]::new)));
            sb.append(">");
        }
        return sb.toString();
    }
}