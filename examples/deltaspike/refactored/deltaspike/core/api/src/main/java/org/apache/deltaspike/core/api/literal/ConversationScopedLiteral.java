package org.apache.deltaspike.core.api.literal;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link ConversationScoped}
 */
public class ConversationScopedLiteral extends AnnotationLiteral<ConversationScoped> implements ConversationScoped
{
    private static final long serialVersionUID = -7672396348051568920L;
}