package org.apache.deltaspike.core.impl.lock;

import org.apache.deltaspike.core.api.lock.Locked;
import org.apache.deltaspike.core.spi.lock.LockedStrategy;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@Locked
@Interceptor
public class LockedInterceptor implements Serializable
{
    @Inject
    private LockedStrategy lockedStrategy;

    @AroundInvoke
    public Object invoke(final InvocationContext ic) throws Exception
    {
        return lockedStrategy.execute(ic);
    }
}