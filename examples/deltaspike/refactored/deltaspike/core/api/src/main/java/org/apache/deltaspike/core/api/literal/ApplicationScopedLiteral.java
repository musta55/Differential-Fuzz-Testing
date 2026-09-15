package org.apache.deltaspike.core.api.literal;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link ApplicationScoped}
 */
public class ApplicationScopedLiteral extends AnnotationLiteral<ApplicationScoped> implements ApplicationScoped
{
    private static final long serialVersionUID = 6582580975876369665L;
}