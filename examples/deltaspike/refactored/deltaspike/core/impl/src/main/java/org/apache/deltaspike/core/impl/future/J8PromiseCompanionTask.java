package org.apache.deltaspike.core.impl.future;

import org.apache.deltaspike.core.util.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

class J8PromiseCompanionTask<T> implements Runnable
{
    private static final Method COMPLETABLE_FUTURE_COMPLETE;
    private static final Method COMPLETABLE_FUTURE_COMPLETE_ERROR;

    static
    {
        Class<?> completableFutureClass = null;
        Method completableFutureComplete = null;
        Method completableFutureCompleteError = null;
        try
        {
            final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            completableFutureClass = classLoader.loadClass("java.util.concurrent.CompletableFuture");
            completableFutureComplete = completableFutureClass.getMethod("complete", Object.class);
            completableFutureCompleteError = completableFutureClass.getMethod("completeExceptionally", Throwable.class);
        }
        catch (final Exception e)
        {
            // not on java 8
        }
        COMPLETABLE_FUTURE_COMPLETE = completableFutureComplete;
        COMPLETABLE_FUTURE_COMPLETE_ERROR = completableFutureCompleteError;
    }

    private Object dep;
    private Callable<T> fn;

    J8PromiseCompanionTask(final Object dep, Callable<T> fn)
    {
        this.dep = dep;
        this.fn = fn;
    }

    public void run()
    {
        try
        {
            COMPLETABLE_FUTURE_COMPLETE.invoke(dep, fn.call());
        }
        catch (Exception e)
        {
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            try
            {
                COMPLETABLE_FUTURE_COMPLETE_ERROR.invoke(dep, cause);
            }
            catch (IllegalAccessException | InvocationTargetException e1)
            {
                throw ExceptionUtils.throwAsRuntimeException(e1.getCause());
            }
        }
    }
}