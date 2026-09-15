package org.apache.deltaspike.core.impl.throttling;

import org.apache.deltaspike.core.api.throttling.Throttled;
import org.apache.deltaspike.core.spi.throttling.ThrottledStrategy;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@Throttled
@Interceptor
public class ThrottledInterceptor implements Serializable
{
    @Inject
    private ThrottledStrategy throttledStrategy;

    @AroundInvoke
    public Object invoke(final InvocationContext ic) throws Exception
    {
        return throttledStrategy.execute(ic);
    }
}