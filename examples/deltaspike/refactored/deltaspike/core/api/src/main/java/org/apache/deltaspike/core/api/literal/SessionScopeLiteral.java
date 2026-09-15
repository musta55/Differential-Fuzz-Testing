package org.apache.deltaspike.core.api.literal;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link SessionScoped}
 */
public class SessionScopeLiteral extends AnnotationLiteral<SessionScoped> implements SessionScoped
{
    private static final long serialVersionUID = -1425877068082208121L;
}