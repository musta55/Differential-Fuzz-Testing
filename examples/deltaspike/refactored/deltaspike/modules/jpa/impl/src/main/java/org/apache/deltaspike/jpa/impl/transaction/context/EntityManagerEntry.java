package org.apache.deltaspike.jpa.impl.transaction.context;

import javax.enterprise.inject.Typed;
import javax.persistence.EntityManager;
import java.lang.annotation.Annotation;

/**
 * Stores a {@link EntityManager} and the qualifier
 */
@Typed()
public class EntityManagerEntry
{
    private final EntityManager entityManager;
    //TODO DELTASPIKE-259 - use the annotation itself + calculate a key for #hashCode and #equals
    private Class<? extends Annotation> qualifier;

    public EntityManagerEntry(EntityManager entityManager, Class<? extends Annotation> qualifier)
    {
        this.entityManager = entityManager;
        this.qualifier = qualifier;
    }

    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    //can be used e.g. by a custom strategy for logging,...
    @SuppressWarnings("UnusedDeclaration")
    public Class<? extends Annotation> getQualifier()
    {
        return qualifier;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!isSameClass(o))
        {
            return false;
        }

        EntityManagerEntry that = (EntityManagerEntry) o;

        return hasSameQualifier(that);
    }

    private boolean isSameClass(Object o)
    {
        return o != null && getClass() == o.getClass();
    }

    private boolean hasSameQualifier(EntityManagerEntry that)
    {
        return qualifier.equals(that.qualifier);
    }

    @Override
    public int hashCode()
    {
        return qualifier.hashCode();
    }
}