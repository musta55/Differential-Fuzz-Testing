package org.apache.deltaspike.core.api.literal;

import javax.enterprise.inject.New;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link New}
 */
public class NewLiteral extends AnnotationLiteral<New> implements New
{
    private static final long serialVersionUID = -4134892777333672942L;

    private final Class<?> value;

    public NewLiteral()
    {
        this(New.class);
    }

    public NewLiteral(Class<?> value)
    {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> value()
    {
        return value;
    }
}