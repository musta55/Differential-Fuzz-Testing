package org.apache.deltaspike.core.api.scope;

import java.io.Serializable;

public interface GroupedConversation extends Serializable
{
    void close();
}