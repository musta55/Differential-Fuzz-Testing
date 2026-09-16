package org.apache.deltaspike.core.api.literal;

import javax.enterprise.inject.Typed;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link javax.enterprise.inject.Typed}
 */
public class TypedLiteral extends AnnotationLiteral<Typed> implements Typed
{
    private static final long serialVersionUID = 6805980497117269525L;

    private final Class<?>[] value;

    public TypedLiteral()
    {
        this(new Class<?>[0]);
    }

    public TypedLiteral(Class<?>[] value)
    {
        this.value = value.clone();
    }

    @Override
    public Class<?>[] value()
    {
        return value.clone();
    }
}