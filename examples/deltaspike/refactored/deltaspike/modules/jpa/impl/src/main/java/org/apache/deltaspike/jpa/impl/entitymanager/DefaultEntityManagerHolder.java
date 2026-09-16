package org.apache.deltaspike.jpa.impl.entitymanager;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;

import org.apache.deltaspike.jpa.spi.entitymanager.ActiveEntityManagerHolder;

/**
 * Empty holder. Override and specialize in using module.
 * Currently only used by the data module.
 */
@Dependent
public class DefaultEntityManagerHolder implements ActiveEntityManagerHolder
{
    @Override
    public void set(EntityManager entityManager)
    {
        throw new UnsupportedOperationException(
                "Default implementation does not store an EntityManager");
    }

    @Override
    public boolean isSet()
    {
        return false;
    }

    @Override
    public EntityManager get()
    {
        return null;
    }

    @Override
    public void dispose()
    {
    }
}