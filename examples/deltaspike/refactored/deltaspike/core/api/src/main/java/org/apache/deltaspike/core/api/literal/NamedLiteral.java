package org.apache.deltaspike.core.api.literal;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

/**
 * Literal for {@link javax.inject.Named} qualifier.
 */
public class NamedLiteral extends AnnotationLiteral<Named> implements Named
{
    private static final long serialVersionUID = -1457223276475846060L;

    private final String value;

    public NamedLiteral(String value)
    {
        this.value = value;
    }

    public NamedLiteral()
    {
        this("");
    }

    @Override public String value()
    {
        return value;
    }
}