package org.apache.deltaspike.core.api.literal;

import javax.enterprise.inject.Model;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link javax.enterprise.inject.Model} annotation.
 */
public class ModelLiteral extends AnnotationLiteral<Model> implements Model
{
    private static final long serialVersionUID = -1828119201454843678L;
}