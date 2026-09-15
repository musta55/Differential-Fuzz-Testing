package org.apache.deltaspike.core.impl.lock;

import org.apache.deltaspike.core.spi.lock.LockedStrategy;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import java.util.concurrent.locks.Lock;

@Dependent
public class DefaultLockedStrategy implements LockedStrategy
{
    @Inject
    private LockSupplierStorage lockSupplierStorage;

    @Override
    public Object execute(InvocationContext ic) throws Exception
    {
        final Lock lock = lockSupplierStorage.getLockSupplier(ic).get();
        try
        {
            return ic.proceed();
        }
        finally
        {
            lock.unlock();
        }
    }
}