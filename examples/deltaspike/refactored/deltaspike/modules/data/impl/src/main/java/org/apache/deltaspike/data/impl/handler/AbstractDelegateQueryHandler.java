package org.apache.deltaspike.data.impl.handler;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.deltaspike.data.spi.DelegateQueryHandler;

public abstract class AbstractDelegateQueryHandler<E> implements DelegateQueryHandler
{

    @Inject
    protected CdiQueryInvocationContext context;

    @SuppressWarnings("unchecked")
    protected Class<E> getEntityClass()
    {
        return (Class<E>) context.getEntityClass();
    }

    protected EntityManager getEntityManager()
    {
        return context.getEntityManager();
    }

}