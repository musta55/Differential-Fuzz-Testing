package org.apache.deltaspike.core.api.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.apache.deltaspike.core.api.message.MessageBundle;

/**
 * Literal for {@link MessageBundle}
 */
public class MessageBundleLiteral extends AnnotationLiteral<MessageBundle> implements MessageBundle
{
    private static final long serialVersionUID = 5116646785333766333L;
}