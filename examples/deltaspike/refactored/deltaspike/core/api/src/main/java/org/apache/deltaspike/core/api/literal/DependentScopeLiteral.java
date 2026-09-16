package org.apache.deltaspike.core.api.literal;

import javax.enterprise.context.Dependent;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link Dependent}
 */
public class DependentScopeLiteral extends AnnotationLiteral<Dependent> implements Dependent
{
    private static final long serialVersionUID = -7408316864366401212L;
}