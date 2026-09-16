package org.apache.deltaspike.core.impl.scope.conversation;

import org.apache.deltaspike.core.api.scope.ConversationGroup;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ConversationKey implements Serializable
{
    private static final long serialVersionUID = 6565204223928766263L;

    private final Class<?> groupKey;

    //HashSet due to Serializable warning in checkstyle rules
    private final HashSet<Annotation> qualifiers;

    public ConversationKey(Class<?> groupKey, Annotation... qualifiers)
    {
        this.groupKey = groupKey;
        this.qualifiers = new HashSet<>();

        //TODO maybe we have to add a real qualifier instead
        for (Annotation qualifier : qualifiers)
        {
            Class<? extends Annotation> annotationType = qualifier.annotationType();

            if (Any.class.isAssignableFrom(annotationType) ||
                    Default.class.isAssignableFrom(annotationType) ||
                    Named.class.isAssignableFrom(annotationType) ||
                    ConversationGroup.class.isAssignableFrom(annotationType))
            {
                //won't be used for this key!
                continue;
            }

            this.qualifiers.add(qualifier);
        }
    }

    public Class<?> getConversationGroup()
    {
        return groupKey;
    }

    public Set<Annotation> getQualifiers()
    {
        return Collections.unmodifiableSet(this.qualifiers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ConversationKey))
        {
            return false;
        }

        ConversationKey that = (ConversationKey) o;

        return Objects.equals(groupKey, that.groupKey) &&
               Objects.equals(qualifiers, that.qualifiers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        int result = groupKey.hashCode();
        result = 31 * result + qualifiers.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("conversation-key\n");

        result.append("\n")
              .append("\tgroup:\t\t")
              .append(this.groupKey.getName())
              .append("\n")
              .append("\tqualifiers:\t");

        if (qualifiers.isEmpty())
        {
            result.append("---");
        }
        else
        {
            for (Annotation qualifier : this.qualifiers)
            {
                result.append(qualifier.annotationType().getName()).append(" ");
            }
        }

        return result.toString();
    }
}