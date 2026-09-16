package org.apache.deltaspike.jpa.impl.transaction.context;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Typed;

/**
 * Holds the information we need store to manage
 * the beans in the {@link TransactionContext}.
 */
@Typed()
public class TransactionBeanEntry<T>
{
    private final Contextual<T> bean;
    private final T contextualInstance;
    private final CreationalContext<T> creationalContext;

    public TransactionBeanEntry(Contextual<T> bean, T contextualInstance, CreationalContext<T> creationalContext)
    {
        this.bean = bean;
        this.contextualInstance = contextualInstance;
        this.creationalContext = creationalContext;
    }

    public Contextual<T> getBean()
    {
        return bean;
    }

    public T getContextualInstance()
    {
        return contextualInstance;
    }

    public CreationalContext<T> getCreationalContext()
    {
        return creationalContext;
    }
}