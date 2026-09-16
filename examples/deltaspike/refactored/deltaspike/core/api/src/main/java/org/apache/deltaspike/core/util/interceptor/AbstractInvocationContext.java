package org.apache.deltaspike.core.util.interceptor;

import javax.enterprise.inject.Typed;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Typed()
public abstract class AbstractInvocationContext<T> implements InvocationContext
{
    protected final T target;
    protected final Method method;
    protected final Object timer;

    protected Object[] parameters;
    protected Map<String, Object> contextData = new HashMap<>();

    protected AbstractInvocationContext(T target, Method method, Object[] parameters, Object timer)
    {
        this.target = target;
        this.method = method;
        this.parameters = parameters;
        this.timer = timer;
    }


    @Override
    public Object getTarget()
    {
        return target;
    }

    @Override
    public Method getMethod()
    {
        return method;
    }

    @Override
    public Object getTimer()
    {
        return timer;
    }

    @Override
    public Object[] getParameters()
    {
        return parameters;
    }

    @Override
    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }

    @Override
    public Map<String, Object> getContextData()
    {
        return contextData;
    }

    // @Override - forward compatibility to interceptors API 1.2
    public Constructor<?> getConstructor()
    {
        return null;
    }
}