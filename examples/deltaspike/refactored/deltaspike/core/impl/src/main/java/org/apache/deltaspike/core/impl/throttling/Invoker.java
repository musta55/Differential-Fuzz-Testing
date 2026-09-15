package org.apache.deltaspike.core.impl.throttling;

import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.interceptor.InvocationContext;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class Invoker
{
    private final int weight;
    private final Semaphore semaphore;
    private final long timeout;

    Invoker(final Semaphore semaphore, final int weight, final long timeout)
    {
        this.semaphore = semaphore;
        this.weight = weight;
        this.timeout = timeout;
    }

    public Object invoke(final InvocationContext context) throws Exception
    {
        if (timeout > 0)
        {
            if (!tryAcquireWithTimeout())
            {
                throw new IllegalStateException(
                    "Can't acquire " + weight + " permits for " + context.getMethod() + " in " + timeout + "ms");
            }
        }
        else
        {
            tryAcquireWithoutTimeout();
        }
        try
        {
            return context.proceed();
        }
        finally
        {
            semaphore.release(weight);
        }
    }

    private boolean tryAcquireWithTimeout()
    {
        try
        {
            return semaphore.tryAcquire(weight, timeout, TimeUnit.MILLISECONDS);
        }
        catch (final InterruptedException e)
        {
            onInterruption(e);
            return false;
        }
    }

    private void tryAcquireWithoutTimeout()
    {
        try
        {
            semaphore.acquire(weight);
        }
        catch (final InterruptedException e)
        {
            onInterruption(e);
        }
    }

    private static void onInterruption(final InterruptedException e)
    {
        Thread.interrupted();
        throw ExceptionUtils.throwAsRuntimeException(e);
    }
}