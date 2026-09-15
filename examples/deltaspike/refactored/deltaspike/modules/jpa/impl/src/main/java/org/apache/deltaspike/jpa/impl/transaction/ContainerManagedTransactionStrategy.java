package org.apache.deltaspike.jpa.impl.transaction;

import org.apache.deltaspike.jpa.spi.transaction.TransactionStrategy;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import javax.interceptor.InvocationContext;

/**
 * <p>{@link org.apache.deltaspike.jpa.spi.transaction.TransactionStrategy} for CMT for the data-module.</p>
 */
@Dependent
@Alternative
@SuppressWarnings("UnusedDeclaration")
public class ContainerManagedTransactionStrategy implements TransactionStrategy
{
    private static final long serialVersionUID = 70354806762739497L;

    @Override
    public Object execute(InvocationContext invocationContext) throws Exception
    {
        return invocationContext.proceed();
    }
}