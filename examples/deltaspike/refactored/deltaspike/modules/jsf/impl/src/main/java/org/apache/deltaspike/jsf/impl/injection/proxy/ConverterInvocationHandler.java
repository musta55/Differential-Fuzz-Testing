package org.apache.deltaspike.jsf.impl.injection.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConverterInvocationHandler implements InvocationHandler
{
    private final DefaultPartialStateHolder defaultPartialStateHolder = new DefaultPartialStateHolder();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        return method.invoke(defaultPartialStateHolder, args);
    }
}