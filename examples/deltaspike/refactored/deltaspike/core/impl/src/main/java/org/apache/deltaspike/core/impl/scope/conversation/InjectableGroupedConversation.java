package org.apache.deltaspike.core.impl.scope.conversation;

import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.core.spi.scope.conversation.GroupedConversationManager;

import javax.enterprise.inject.Typed;
import java.lang.annotation.Annotation;
import java.util.Set;

@Typed()
class InjectableGroupedConversation implements GroupedConversation
{
    private static final long serialVersionUID = -3909049219127821425L;

    private final ConversationKey conversationKey;
    private final GroupedConversationManager conversationManager;

    InjectableGroupedConversation(ConversationKey conversationKey, GroupedConversationManager conversationManager)
    {
        this.conversationManager = conversationManager;
        this.conversationKey = conversationKey;
    }

    @Override
    public void close()
    {
        Annotation[] qualifiersArray = this.conversationKey.getQualifiers().toArray(new Annotation[0]);
        this.conversationManager.closeConversation(this.conversationKey.getConversationGroup(), qualifiersArray);
    }
}