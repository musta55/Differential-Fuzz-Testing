package org.apache.deltaspike.core.api.literal;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link RequestScoped}
 */
public class RequestScopedLiteral extends AnnotationLiteral<RequestScoped> implements RequestScoped
{
    private static final long serialVersionUID = -6365776352042023537L;
}