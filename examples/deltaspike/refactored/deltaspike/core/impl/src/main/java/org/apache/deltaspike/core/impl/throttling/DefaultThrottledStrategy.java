package org.apache.deltaspike.core.impl.throttling;

import org.apache.deltaspike.core.spi.throttling.ThrottledStrategy;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;

@Dependent
public class DefaultThrottledStrategy implements ThrottledStrategy
{
    @Inject
    private InvokerStorage metadata;

    @Override
    public Object execute(InvocationContext ic) throws Exception
    {
        return metadata.getOrCreateInvoker(ic).invoke(ic);
    }
}