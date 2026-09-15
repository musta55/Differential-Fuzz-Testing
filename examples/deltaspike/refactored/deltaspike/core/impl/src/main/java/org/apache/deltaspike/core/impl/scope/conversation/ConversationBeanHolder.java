package org.apache.deltaspike.core.impl.scope.conversation;

import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.core.impl.scope.AbstractBeanHolder;

@WindowScoped
public class ConversationBeanHolder extends AbstractBeanHolder<ConversationKey>
{
    private static final long serialVersionUID = 6313493410718133308L;
}