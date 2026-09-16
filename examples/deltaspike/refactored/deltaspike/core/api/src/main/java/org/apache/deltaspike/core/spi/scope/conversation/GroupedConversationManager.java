package org.apache.deltaspike.core.spi.scope.conversation;

import org.apache.deltaspike.core.util.context.ContextualStorage;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;

public interface GroupedConversationManager extends Serializable
{
    /**
     * @param conversationGroup group of the conversation in question
     * @param qualifiers        optional qualifiers for the conversation
     * @return the removed conversation - null otherwise
     */
    ContextualStorage closeConversation(Class<?> conversationGroup, Annotation... qualifiers);

    /**
     * destroys all conversation of a group independent of the qualifiers
     *
     * @param conversationGroup group of the conversation in question
     * @return the removed storages - null otherwise
     */
    Set<ContextualStorage> closeConversationGroup(Class<?> conversationGroup);

    /**
     * invalidate all conversations immediately (within the current window)
     */
    void closeConversations();
}