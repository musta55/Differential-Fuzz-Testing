package org.apache.deltaspike.data.impl.tx;

import org.apache.deltaspike.core.util.interceptor.AbstractInvocationContext;
import org.apache.deltaspike.data.impl.handler.CdiQueryInvocationContext;

public abstract class InvocationContextWrapper extends AbstractInvocationContext<Object>
{
    public InvocationContextWrapper(CdiQueryInvocationContext context)
    {
        super(context.getProxy(), context.getMethod(), context.getMethodParameters(), null);
    }
}