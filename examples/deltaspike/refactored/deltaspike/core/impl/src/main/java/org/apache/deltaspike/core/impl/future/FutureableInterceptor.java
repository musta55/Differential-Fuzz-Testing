package org.apache.deltaspike.core.impl.future;

import org.apache.deltaspike.core.api.future.Futureable;
import org.apache.deltaspike.core.spi.future.FutureableStrategy;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@Interceptor
@Futureable
public class FutureableInterceptor implements Serializable
{
    @Inject
    private FutureableStrategy futureableStrategy;

    @AroundInvoke
    public Object invoke(final InvocationContext ic) throws Exception
    {
        return futureableStrategy.execute(ic);
    }
}