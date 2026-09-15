package org.apache.deltaspike.core.impl.scope.conversation;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.impl.scope.DeltaSpikeContextExtension;
import org.apache.deltaspike.core.spi.scope.conversation.GroupedConversationManager;
import org.apache.deltaspike.core.util.context.ContextualStorage;

import javax.enterprise.inject.Typed;
import java.lang.annotation.Annotation;
import java.util.Set;

@Typed()
class InjectableGroupedConversationManager implements GroupedConversationManager
{
    private transient volatile GroupedConversationManager conversationManager;

    InjectableGroupedConversationManager(GroupedConversationManager conversationManager)
    {
        this.conversationManager = conversationManager;
    }

    private GroupedConversationManager getConversationManager()
    {
        if (conversationManager == null)
        {
            conversationManager = BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getConversationContext();
        }
        return conversationManager;
    }

    @Override
    public ContextualStorage closeConversation(Class<?> conversationGroup, Annotation... qualifiers)
    {
        return getConversationManager().closeConversation(conversationGroup, qualifiers);
    }

    @Override
    public Set<ContextualStorage> closeConversationGroup(Class<?> conversationGroup)
    {
        return getConversationManager().closeConversationGroup(conversationGroup);
    }

    @Override
    public void closeConversations()
    {
        getConversationManager().closeConversations();
    }
}